-- Insert test user for development/testing
INSERT INTO users (id, username, email, balance, created_at, updated_at)
VALUES (
    'test-user-01',
    'user1',
    'user1@f1betting.com',
    100.00,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;