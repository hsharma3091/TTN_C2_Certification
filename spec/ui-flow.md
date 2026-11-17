# UI Flow

## Home `/`
- Search input (debounced) → `q`
- Status dropdown filter → `status` (ALL clears filter)
- Table: tickets; click row → detail

## Detail `/tickets/[id]`
- View all fields; form to save title, description, priority, assignee
- Status dropdown shows only **legal** next statuses from API `GET .../transitions` or client mirrors policy
- Comments list + add comment form
- Show banner on API errors (validation, 409 transition)

## Empty / error states
- 404 ticket: message + link home
- Field errors from `errors[]` array