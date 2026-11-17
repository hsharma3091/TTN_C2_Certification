"use client";

import { useCallback, useEffect, useState } from "react";
import Link from "next/link";
import {
  ApiError,
  createTicket,
  listTickets,
  Ticket,
  TicketPriority,
} from "@/lib/api";

const STATUSES = [
  "",
  "OPEN",
  "IN_PROGRESS",
  "RESOLVED",
  "CLOSED",
  "CANCELLED",
] as const;

export default function HomePage() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [q, setQ] = useState("");
  const [status, setStatus] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [showCreate, setShowCreate] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<TicketPriority>("MEDIUM");

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await listTickets({
        q: q.trim() || undefined,
        status: status || undefined,
      });
      setTickets(data);
    } catch (e) {
      setError(e instanceof ApiError ? e.message : "Failed to load tickets");
    } finally {
      setLoading(false);
    }
  }, [q, status]);

  useEffect(() => {
    const t = setTimeout(load, 300);
    return () => clearTimeout(t);
  }, [load]);

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    try {
      const t = await createTicket({ title, description, priority });
      setTitle("");
      setDescription("");
      setShowCreate(false);
      window.location.href = `/tickets/${t.id}`;
    } catch (err) {
      const ae = err as ApiError;
      setError(
        ae.errors?.map((x) => `${x.field}: ${x.message}`).join("; ") ||
          ae.message
      );
    }
  }

  return (
    <main className="container">
      {error && <div className="error-banner">{error}</div>}

      <div className="row" style={{ marginBottom: "1rem" }}>
        <div className="field">
          <label htmlFor="search">Search</label>
          <input
            id="search"
            placeholder="Keyword in title or description"
            value={q}
            onChange={(e) => setQ(e.target.value)}
          />
        </div>
        <div className="field" style={{ maxWidth: 200 }}>
          <label htmlFor="status">Status</label>
          <select
            id="status"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="">All</option>
            {STATUSES.filter(Boolean).map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>
        <button type="button" onClick={() => setShowCreate(!showCreate)}>
          {showCreate ? "Cancel" : "New ticket"}
        </button>
      </div>

      {showCreate && (
        <form className="card" onSubmit={handleCreate} style={{ marginBottom: "1rem" }}>
          <h2 style={{ marginTop: 0 }}>Create ticket</h2>
          <div className="field" style={{ marginBottom: "0.75rem" }}>
            <label htmlFor="title">Title *</label>
            <input
              id="title"
              required
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>
          <div className="field" style={{ marginBottom: "0.75rem" }}>
            <label htmlFor="desc">Description</label>
            <textarea
              id="desc"
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div className="field" style={{ marginBottom: "0.75rem", maxWidth: 200 }}>
            <label htmlFor="pri">Priority</label>
            <select
              id="pri"
              value={priority}
              onChange={(e) => setPriority(e.target.value as TicketPriority)}
            >
              {(["LOW", "MEDIUM", "HIGH", "URGENT"] as const).map((p) => (
                <option key={p} value={p}>
                  {p}
                </option>
              ))}
            </select>
          </div>
          <button type="submit">Create</button>
        </form>
      )}

      <div className="card">
        {loading ? (
          <p>Loading…</p>
        ) : tickets.length === 0 ? (
          <p>No tickets match your filters.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Status</th>
                <th>Priority</th>
                <th>Assignee</th>
              </tr>
            </thead>
            <tbody>
              {tickets.map((t) => (
                <tr key={t.id}>
                  <td>{t.id}</td>
                  <td>
                    <Link href={`/tickets/${t.id}`}>{t.title}</Link>
                  </td>
                  <td>
                    <span className="badge">{t.status}</span>
                  </td>
                  <td>{t.priority}</td>
                  <td>{t.assignee ?? "—"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </main>
  );
}