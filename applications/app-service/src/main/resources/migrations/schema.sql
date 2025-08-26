DROP TABLE IF EXISTS loan_requests;

CREATE TABLE public.loan_requests (
    loan_id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(19,2) NOT NULL,
    loan_term INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    state_id BIGINT NOT NULL,
    loan_type_id BIGINT NOT NULL,
    customer_id BIGINT
);

DROP TABLE IF EXISTS loan_requests_status;

CREATE TABLE public.loan_requests_status (
    loan_state_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

DROP TABLE IF EXISTS loan_requests_types;

CREATE TABLE public.loan_requests_types (
    loan_type_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    min_amount NUMERIC(19,2) NOT NULL,
    max_amount NUMERIC(19,2) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL,
    auto_validation BOOLEAN DEFAULT FALSE
);


