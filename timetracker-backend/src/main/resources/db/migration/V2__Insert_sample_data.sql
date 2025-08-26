-- Insert sample employees
INSERT INTO employees (first_name, last_name, email, keycloak_id, role, status, department, position, hire_date, vacation_days_per_year, remaining_vacation_days) VALUES
('Mario', 'Rossi', 'mario.rossi@company.com', 'keycloak-mario-123', 'ADMIN', 'ACTIVE', 'IT', 'System Administrator', '2020-01-15 09:00:00', 25, 20),
('Giulia', 'Bianchi', 'giulia.bianchi@company.com', 'keycloak-giulia-456', 'HR_MANAGER', 'ACTIVE', 'HR', 'HR Manager', '2019-03-10 09:00:00', 28, 25),
('Luca', 'Verdi', 'luca.verdi@company.com', 'keycloak-luca-789', 'MANAGER', 'ACTIVE', 'Development', 'Project Manager', '2021-06-01 09:00:00', 25, 18),
('Anna', 'Neri', 'anna.neri@company.com', 'keycloak-anna-101', 'EMPLOYEE', 'ACTIVE', 'Development', 'Senior Developer', '2022-02-14 09:00:00', 25, 22),
('Marco', 'Gialli', 'marco.gialli@company.com', 'keycloak-marco-202', 'EMPLOYEE', 'ACTIVE', 'Development', 'Junior Developer', '2023-01-10 09:00:00', 25, 25),
('Sara', 'Blu', 'sara.blu@company.com', 'keycloak-sara-303', 'EMPLOYEE', 'ACTIVE', 'QA', 'QA Specialist', '2022-09-05 09:00:00', 25, 20);

-- Insert sample projects
INSERT INTO projects (code, name, description, status, start_date, end_date, client_name) VALUES
('PROJ001', 'E-Commerce Platform', 'Development of a new e-commerce platform for retail client', 'ACTIVE', '2024-01-01 09:00:00', '2024-12-31 18:00:00', 'RetailCorp SpA'),
('PROJ002', 'Mobile Banking App', 'Mobile application for banking services', 'ACTIVE', '2024-02-15 09:00:00', '2024-08-30 18:00:00', 'BankItalia'),
('PROJ003', 'Internal CRM System', 'Customer relationship management system for internal use', 'ACTIVE', '2024-03-01 09:00:00', NULL, 'Internal'),
('PROJ004', 'Legacy System Migration', 'Migration of legacy systems to cloud infrastructure', 'COMPLETED', '2023-06-01 09:00:00', '2023-12-15 18:00:00', 'TechSolutions Srl');

-- Insert sample tasks
INSERT INTO tasks (code, name, description, status, priority, estimated_hours, due_date, project_id, assigned_employee_id) VALUES
('T001', 'Database Design', 'Design and implement database schema', 'COMPLETED', 'HIGH', 40.0, '2024-01-15 18:00:00', 1, 4),
('T002', 'User Authentication', 'Implement user authentication and authorization', 'IN_PROGRESS', 'HIGH', 32.0, '2024-01-30 18:00:00', 1, 4),
('T003', 'Product Catalog', 'Develop product catalog functionality', 'OPEN', 'MEDIUM', 60.0, '2024-02-15 18:00:00', 1, 5),
('T004', 'Payment Integration', 'Integrate payment gateway', 'OPEN', 'HIGH', 48.0, '2024-02-28 18:00:00', 1, 4),
('T005', 'Mobile UI Design', 'Design mobile user interface', 'IN_PROGRESS', 'MEDIUM', 80.0, '2024-03-15 18:00:00', 2, 5),
('T006', 'API Development', 'Develop REST APIs for mobile app', 'OPEN', 'HIGH', 120.0, '2024-04-01 18:00:00', 2, 4),
('T007', 'Contact Management', 'Implement contact management features', 'COMPLETED', 'MEDIUM', 35.0, '2024-03-20 18:00:00', 3, 4),
('T008', 'Reporting Module', 'Develop reporting and analytics module', 'OPEN', 'LOW', 45.0, '2024-04-15 18:00:00', 3, 5);

-- Assign employees to projects
INSERT INTO project_employees (project_id, employee_id) VALUES
(1, 3), -- Luca (Manager) to E-Commerce Platform
(1, 4), -- Anna to E-Commerce Platform
(1, 5), -- Marco to E-Commerce Platform
(2, 3), -- Luca (Manager) to Mobile Banking App
(2, 4), -- Anna to Mobile Banking App
(2, 5), -- Marco to Mobile Banking App
(3, 4), -- Anna to Internal CRM System
(3, 5), -- Marco to Internal CRM System
(4, 4); -- Anna to Legacy System Migration

-- Insert sample time entries
INSERT INTO time_entries (employee_id, task_id, type, entry_date, start_time, end_time, hours, description, status) VALUES
(4, 1, 'TASK', '2024-01-10 08:00:00', '2024-01-10 09:00:00', '2024-01-10 17:00:00', 8.0, 'Worked on database schema design', 'APPROVED'),
(4, 1, 'TASK', '2024-01-11 08:00:00', '2024-01-11 09:00:00', '2024-01-11 17:00:00', 8.0, 'Continued database design and created ER diagrams', 'APPROVED'),
(4, 2, 'TASK', '2024-01-15 08:00:00', '2024-01-15 09:00:00', '2024-01-15 13:00:00', 4.0, 'Started implementing JWT authentication', 'PENDING'),
(5, 3, 'TASK', '2024-01-12 08:00:00', '2024-01-12 09:00:00', '2024-01-12 17:00:00', 8.0, 'Research on product catalog requirements', 'APPROVED'),
(5, 5, 'TASK', '2024-02-20 08:00:00', '2024-02-20 09:00:00', '2024-02-20 17:00:00', 8.0, 'Mobile UI mockups and wireframes', 'PENDING'),
(4, NULL, 'PRESENCE', '2024-01-10 08:00:00', '2024-01-10 09:00:00', '2024-01-10 17:00:00', 8.0, 'Regular work day', 'APPROVED'),
(4, NULL, 'PRESENCE', '2024-01-11 08:00:00', '2024-01-11 09:00:00', '2024-01-11 17:00:00', 8.0, 'Regular work day', 'APPROVED'),
(5, NULL, 'PRESENCE', '2024-01-12 08:00:00', '2024-01-12 09:00:00', '2024-01-12 17:00:00', 8.0, 'Regular work day', 'APPROVED');

-- Insert sample vacation requests
INSERT INTO vacation_requests (employee_id, start_date, end_date, type, reason, status, approved_by, approved_at) VALUES
(4, '2024-03-15', '2024-03-22', 'ANNUAL_LEAVE', 'Spring vacation with family', 'APPROVED', 'keycloak-luca-789', '2024-02-20 10:30:00'),
(5, '2024-04-01', '2024-04-05', 'ANNUAL_LEAVE', 'Easter holidays', 'PENDING', NULL, NULL),
(6, '2024-02-10', '2024-02-12', 'SICK_LEAVE', 'Flu symptoms', 'APPROVED', NULL, '2024-02-09 14:00:00'),
(4, '2024-07-15', '2024-07-29', 'ANNUAL_LEAVE', 'Summer vacation', 'PENDING', NULL, NULL),
(5, '2024-12-23', '2024-12-31', 'ANNUAL_LEAVE', 'Christmas holidays', 'APPROVED', 'keycloak-luca-789', '2024-11-15 16:00:00');

-- Insert a vacation request with skipped approval
INSERT INTO vacation_requests (employee_id, start_date, end_date, type, reason, status, approval_skipped, approval_skip_label) VALUES
(6, '2024-01-20', '2024-01-21', 'PERSONAL_LEAVE', 'Family emergency', 'APPROVED', TRUE, 'Emergency - Approval Skipped');

-- Update remaining vacation days based on approved requests
UPDATE employees SET remaining_vacation_days = remaining_vacation_days - 8 WHERE id = 4; -- Anna: 8 days approved
UPDATE employees SET remaining_vacation_days = remaining_vacation_days - 9 WHERE id = 5; -- Marco: 9 days approved  
UPDATE employees SET remaining_vacation_days = remaining_vacation_days - 3 WHERE id = 6; -- Sara: 3 days (2 sick + 2 personal - 1 overlap)
