"use client";

import { useCallback, useEffect, useState } from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import {
  addComment,
  ApiError,
  Comment,
  getTicket,
  listComments,
  Ticket,
  TicketPriority,
  TicketStatus,
  transitionTicket,
  updateTicket,
} from "@/lib/api";

export default function TicketDetailPage() {
  const params = useParams();
  const id = String(params.id);

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<TicketPriority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [commentBody, setCommentBody] = useState("");
  const [commentAuthor, setCommentAuthor] = useState("");

  const load = useCallback(async () => {
    setError(null);
    try {
      const [t, c] = await Promise.all([getTicket(id), listComments(id)]);
      setTicket(t);
      setTitle(t.title);
      setDescription(t.description ?? "");
      setPriority(t.priority);
      setAssignee(t.assignee ?? "");
      setComments(c);
    } catch (e) {
      setError(e instanceof ApiError ? e.message : "Failed to load");
      setTicket(null);
    }
  }, [id]);

  useEffect(() => {
    load();
  }, [load]);

  function showApiError(err: unknown) {
    const ae = err as ApiError;
    setError(
      ae.errors?.map((x) => `${x.field}: ${x.message}`).join("; ") ||
        ae.message
    );
  }

  async function saveFields(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    try {
      const t = await updateTicket(id, {
        title,
        description,
        priority,
        assignee: assignee.trim() || "",
      });
      setTicket(t);
    } catch (err) {
      showApiError(err);
    }
  }

  async function changeStatus(status: TicketStatus) {
    setError(null);
    try {
      const t = await transitionTicket(id, status);
      setTicket(t);
    } catch (err) {
      showApiError(err);
    }
  }

  async function submitComment(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    try {
      await addComment(id, {
        body: commentBody,
        author: commentAuthor || undefined,
      });
      setCommentBody("");
      setComments(await listComments(id));
    } catch (err) {
      showApiError(err);
    }
  }

  if (!ticket && !error) {
    return (
      <main className="container">
        <p>Loading…</p>
      </main>
    );
  }

  if (!ticket) {
    return (
      <main className="container">
        <div className="error-banner">{error}</div>
        <Link href="/">← Back to list</Link>
      </main>
    );
  }

  return (
    <main className="container">
      <p>
        <Link href="/">← Back to list</Link>
      </p>

      {error && <div className="error-banner">{error}</div>}

      <div className="card" style={{ marginBottom: "1rem" }}>
        <h2 style={{ marginTop: 0 }}>
          #{ticket.id} — <span className="badge">{ticket.status}</span>
        </h2>
        <form onSubmit={saveFields}>
          <div className="field" style={{ marginBottom: "0.75rem" }}>
            <label htmlFor="title">Title</label>
            <input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>
          <div className="field" style={{ marginBottom: "0.75rem" }}>
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div className="row">
            <div className="field">
              <label htmlFor="priority">Priority</label>
              <select
                id="priority"
                value={priority}
                onChange={(e) =>
                  setPriority(e.target.value as TicketPriority)
                }
              >
                {(["LOW", "MEDIUM", "HIGH", "URGENT"] as const).map((p) => (
                  <option key={p} value={p}>
                    {p}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="assignee">Assignee</label>
              <input
                id="assignee"
                value={assignee}
                onChange={(e) => setAssignee(e.target.value)}
                placeholder="email or name"
              />
            </div>
          </div>
          <button type="submit" style={{ marginTop: "0.75rem" }}>
            Save changes
          </button>
        </form>

        {ticket.allowedTransitions.length > 0 && (
          <div style={{ marginTop: "1.25rem" }}>
            <h3>Status transition</h3>
            <div className="row">
              {ticket.allowedTransitions.map((s) => (
                <button
                  key={s}
                  type="button"
                  className="secondary"
                  onClick={() => changeStatus(s)}
                >
                  → {s}
                </button>
              ))}
            </div>
          </div>
        )}
      </div>

      <div className="card">
        <h3 style={{ marginTop: 0 }}>Comments</h3>
        {comments.length === 0 ? (
          <p style={{ color: "var(--muted)" }}>No comments yet.</p>
        ) : (
          <ul style={{ paddingLeft: "1.1rem" }}>
            {comments.map((c) => (
              <li key={c.id} style={{ marginBottom: "0.75rem" }}>
                <strong>{c.author}</strong>{" "}
                <span style={{ color: "var(--muted)", fontSize: "0.85rem" }}>
                  {new Date(c.createdAt).toLocaleString()}
                </span>
                <p style={{ margin: "0.25rem 0 0" }}>{c.body}</p>
              </li>
            ))}
          </ul>
        )}
        <form onSubmit={submitComment} style={{ marginTop: "1rem" }}>
          <div className="field" style={{ marginBottom: "0.5rem" }}>
            <label htmlFor="cbody">Add comment</label>
            <textarea
              id="cbody"
              rows={2}
              required
              value={commentBody}
              onChange={(e) => setCommentBody(e.target.value)}
            />
          </div>
          <div className="field" style={{ marginBottom: "0.5rem", maxWidth: 240 }}>
            <label htmlFor="author">Author (optional)</label>
            <input
              id="author"
              value={commentAuthor}
              onChange={(e) => setCommentAuthor(e.target.value)}
            />
          </div>
          <button type="submit">Post comment</button>
        </form>
      </div>
    </main>
  );
}