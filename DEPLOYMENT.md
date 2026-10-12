# Academic persistence verification

Use Java 21 and the owning educk-academic-db migrations through V2.3.
Set DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD and JWT_SECRET explicitly.
JWT_SECRET must match Identity. The signed userId and TEACHER role identify the
actor; only the active subject's assigned teacher may create its grades.

Run mvn verify -Dapi.version=1.44 -Dacademic.db.path=/path/to/educk-academic-db
with Docker available. The integration test uses disposable PostgreSQL 16 and
checks decimal precision, zero scores, teacher isolation, business/outbox
atomicity and rollback. Test fixture SQL is executed only in the test database.

This slice persists PENDING events; it does not implement the outbox relay,
notification consumer, complete API contract or end-to-end sprint acceptance.
