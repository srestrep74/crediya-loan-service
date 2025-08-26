INSERT INTO public.loan_requests_status (name, description) VALUES
('PENDING_REVIEW', 'The loan request is pending review'),
('APPROVED', 'The loan request has been approved'),
('REJECTED', 'The loan request has been rejected');

INSERT INTO public.loan_requests_types (name, min_amount, max_amount, interest_rate, auto_validation) VALUES
('PERSONAL_LOAN', 1000.00, 50000.00, 12.50, TRUE),
('MORTGAGE_LOAN', 20000.00, 500000.00, 6.75, FALSE),
('VEHICLE_LOAN', 5000.00, 100000.00, 8.25, TRUE),
('EDUCATION_LOAN', 2000.00, 80000.00, 5.50, FALSE),
('BUSINESS_LOAN', 10000.00, 250000.00, 10.00, FALSE);