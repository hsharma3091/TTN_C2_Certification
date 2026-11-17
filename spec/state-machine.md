# State Machine

## States
`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`

## Allowed transitions
| From | To |
|------|-----|
| OPEN | IN_PROGRESS, CANCELLED |
| IN_PROGRESS | RESOLVED, CANCELLED |
| RESOLVED | CLOSED |
| CLOSED | _(none)_ |
| CANCELLED | _(none)_ |

## Rejected examples
- CLOSED → OPEN
- RESOLVED → OPEN
- CANCELLED → OPEN
- OPEN → CLOSED (must go via IN_PROGRESS, RESOLVED)
- RESOLVED → CANCELLED

## Implementation note
`StatusTransitionPolicy.canTransition(from, to)` is single source of truth; integration tests must mirror this table.