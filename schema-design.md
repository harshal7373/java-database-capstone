# MySQL Database Schema Design

## Database Name
hospital_management

## Tables

### 1. doctor
| Column Name   | Data Type      | Constraints                  |
|---------------|---------------|------------------------------|
| doctor_id     | INT           | PRIMARY KEY, AUTO_INCREMENT  |
| doctor_name   | VARCHAR(100)  | NOT NULL                     |
| specialization| VARCHAR(100)  | NOT NULL                     |
| phone         | VARCHAR(15)   | UNIQUE                       |
| email         | VARCHAR(100)  | UNIQUE                       |

### 2. patient
| Column Name   | Data Type      | Constraints                  |
|---------------|---------------|------------------------------|
| patient_id    | INT           | PRIMARY KEY, AUTO_INCREMENT  |
| patient_name  | VARCHAR(100)  | NOT NULL                     |
| age           | INT           | NOT NULL                     |
| gender        | VARCHAR(10)   | NOT NULL                     |
| phone         | VARCHAR(15)   | UNIQUE                       |
| email         | VARCHAR(100)  | UNIQUE                       |

### 3. appointment
| Column Name       | Data Type      | Constraints                          |
|-------------------|---------------|--------------------------------------|
| appointment_id    | INT           | PRIMARY KEY, AUTO_INCREMENT          |
| appointment_date  | DATE          | NOT NULL                             |
| appointment_time  | TIME          | NOT NULL                             |
| status            | VARCHAR(20)   | DEFAULT 'Scheduled'                  |
| doctor_id         | INT           | FOREIGN KEY REFERENCES doctor(doctor_id) |
| patient_id        | INT           | FOREIGN KEY REFERENCES patient(patient_id) |

## Relationships
- One doctor can have many appointments.
- One patient can book many appointments.
- Appointment table connects doctor and patient.