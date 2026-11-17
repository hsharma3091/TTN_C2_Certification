# API Contract

See `rules/api-standards.md` for full table.

## Create ticket
`POST /api/v1/tickets`
```json
{ "title": "Login broken", "description": "...", "priority": "HIGH", "assignee": "alice@example.com" }
```
Response `201` with ticket body including `id`, `status: "OPEN"`.

## List
`GET /api/v1/tickets?status=OPEN&q=login`

## Update
`PATCH /api/v1/tickets/{id}`
```json
{ "title": "...", "description": "...", "priority": "MEDIUM", "assignee": "bob@example.com" }
```
Null fields ignored (partial update).

## Transition
`POST /api/v1/tickets/{id}/transitions`
```json
{ "status": "IN_PROGRESS" }
```

## Comment
`POST /api/v1/tickets/{id}/comments`
```json
{ "body": "Investigating", "author": "alice" }
```