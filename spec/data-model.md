# Data Model

## Ticket
| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT PK | auto |
| title | VARCHAR(200) | not null |
| description | TEXT | nullable |
| status | VARCHAR(32) | enum |
| priority | VARCHAR(16) | enum |
| assignee | VARCHAR(120) | nullable |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

## Comment
| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT PK | |
| ticket_id | BIGINT FK | |
| body | TEXT | not null |
| author | VARCHAR(120) | default "system" |
| created_at | TIMESTAMP | |

## Indexes
- `ticket(status)`
- Full-text search: LIKE on title/description for exercise (Postgres `ILIKE` / H2 upper()).