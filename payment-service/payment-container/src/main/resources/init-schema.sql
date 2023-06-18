DROP SCHEMA IF EXISTS payment CASCADE;

CREATE SCHEMA payment;

CREATE EXTENSION IF NOT EXISTS 'uuid-ossp';

DROP TYPE IF EXISTS payment_status;

CREATE TYPE payment_status AS ENUM (
  'COMPLETED',
  'CANCELLED',
  'FAILED'
);

DROP TABLE IF EXISTS "payment".payment CASCADE;

CREATE TABLE payment (
  id UUID NOT NULL,
  customer_id UUID NOT NULL,
  order_id UUID NOT NULL,
  price NUMERIC(10, 2) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  status payment_status NOT NULL,
  CONSTRAINT payment_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "payment".credit_entry CASCADE;

CREATE TABLE credit_entry (
  id UUID NOT NULL,
  customer_id UUID NOT NULL,
  total_credit_amount NUMERIC(10, 2) NOT NULL,
  CONSTRAINT credit_entry_pk PRIMARY KEY (id)
);

DROP TYPE IF EXISTS transaction_type;

CREATE TYPE transaction_type AS ENUM (
  'CREDIT',
  'DEBIT'
);

DROP TABLE IF EXISTS "payment".credit_history CASCADE;

CREATE TABLE credit_history (
  id UUID NOT NULL,
  customer_id UUID NOT NULL,
  amount NUMERIC(10, 2) NOT NULL,
  type transaction_type NOT NULL,
  CONSTRAINT credit_history_pk PRIMARY KEY (id)
);