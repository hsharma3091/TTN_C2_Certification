# API Standards

## Versioning
- Base path: `/api/v1`

## Resources
| Method | Path | Description |
|--------|------|-------------|
| POST | `/tickets` | Create ticket |
| GET | `/tickets` | List (query: `status`, `q`) |
| GET | `/tickets/{id}` | Detail |
| PATCH | `/tickets/{id}` | Update title, description, priority, assignee |
| POST | `/tickets/{id}/transitions` | Change status (body: `{ "status": "IN_PROGRESS" }`) |
| POST | `/tickets/{id}/comments` | Add comment |
| GET | `/tickets/{id}/comments` | List comments |

## Errors (RFC 7807-style JSON)
```json
{
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "title must not be blank",
  "errors": [{ "field": "title", "message": "must not be blank" }]
}
```

## Status codes
- `201` create
- `200` success
- `400` validation
- `404` not found
- `409` invalid state transition
- `422` optional for semantic errors

## Pagination (future)
- `page`, `size` — not required for exercise; list all with reasonable limit server-side (e.g. 500)

## Query params
- `q` — case-insensitive keyword search on title + description
- `status` — exact enum match