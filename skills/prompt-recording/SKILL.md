# Prompt Recording (Kiro / Cursor)

After each meaningful AI session:

1. Append a dated file under `.specstory/history/YYYY-MM-DD-<topic>.md` with user prompt (paraphrased), spec refs used, and outcome.
2. Add one line to `docs/prompt-history.md` index table.
3. If AI output was wrong, add a row to `docs/ai-review-log.md` before merging code.

Never store secrets in prompt history files.