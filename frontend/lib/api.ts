export type TicketStatus =
  | "OPEN"
  | "IN_PROGRESS"
  | "RESOLVED"
  | "CLOSED"
  | "CANCELLED";

export type TicketPriority = "LOW" | "MEDIUM" | "HIGH" | "URGENT";

export interface Ticket {
  id: number;
  title: string;
  description: string | null;
  status: TicketStatus;
  priority: TicketPriority;
  assignee: string | null;
  createdAt: string;
  updatedAt: string;
  allowedTransitions: TicketStatus[];
}

export interface Comment {
  id: number;
  ticketId: number;
  body: string;
  author: string;
  createdAt: string;
}

export interface FieldError {
  field: string;
  message: string;
}

export class ApiError extends Error {
  status: number;
  title?: string;
  errors?: FieldError[];

  constructor(
    status: number,
    detail: string,
    title?: string,
    errors?: FieldError[]
  ) {
    super(detail);
    this.status = status;
    this.title = title;
    this.errors = errors;
  }
}

async function parseError(res: Response): Promise<ApiError> {
  let body: Record<string, unknown> = {};
  try {
    body = await res.json();
  } catch {
    /* empty */
  }
  const errors = body.errors as FieldError[] | undefined;
  const detail =
    (body.detail as string) ||
    (errors?.[0]?.message ?? res.statusText) ||
    "Request failed";
  return new ApiError(
    res.status,
    detail,
    body.title as string | undefined,
    errors
  );
}

const apiBase = "/api/v1";

export async function listTickets(params: {
  status?: string;
  q?: string;
}): Promise<Ticket[]> {
  const sp = new URLSearchParams();
  if (params.status) sp.set("status", params.status);
  if (params.q) sp.set("q", params.q);
  const qs = sp.toString();
  const res = await fetch(`${apiBase}/tickets${qs ? `?${qs}` : ""}`);
  if (!res.ok) throw await parseError(res);
  return res.json();
}

export async function getTicket(id: string): Promise<Ticket> {
  const res = await fetch(`${apiBase}/tickets/${id}`);
  if (!res.ok) throw await parseError(res);
  return res.json();
}

export async function createTicket(body: {
  title: string;
  description?: string;
  priority?: TicketPriority;
  assignee?: string;
}): Promise<Ticket> {
  const res = await fetch(`${apiBase}/tickets`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw await parseError(res);
  return res.json();
}

export async function updateTicket(
  id: string,
  body: Partial<{
    title: string;
    description: string;
    priority: TicketPriority;
    assignee: string;
  }>
): Promise<Ticket> {
  const res = await fetch(`${apiBase}/tickets/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw await parseError(res);
  return res.json();
}

export async function transitionTicket(
  id: string,
  status: TicketStatus
): Promise<Ticket> {
  const res = await fetch(`${apiBase}/tickets/${id}/transitions`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status }),
  });
  if (!res.ok) throw await parseError(res);
  return res.json();
}

export async function listComments(ticketId: string): Promise<Comment[]> {
  const res = await fetch(`${apiBase}/tickets/${ticketId}/comments`);
  if (!res.ok) throw await parseError(res);
  return res.json();
}

export async function addComment(
  ticketId: string,
  body: { body: string; author?: string }
): Promise<Comment> {
  const res = await fetch(`${apiBase}/tickets/${ticketId}/comments`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw await parseError(res);
  return res.json();
}