CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE enum_level AS ENUM ('LOW', 'MEDIUM', 'HIGH');

CREATE TABLE userprofile (
    id_userprofile_pk uuid not null default uuid_generate_v4() PRIMARY KEY,
    username varchar(64) not null,
    birthday date not null check(((current_date - birthday) / 365) >= 14),
    encoded_email_un varchar(256) not null UNIQUE,
    encoded_password char(60) not null check(length(encoded_password) = 60),
    created timestamp with time zone not null default current_timestamp
);

CREATE TABLE verify_email (
    id_verify_email_pfk uuid not null PRIMARY KEY,
    encoded_activation_key char(192) not null check(length(encoded_activation_key) = 192),
    created timestamp with time zone not null default current_timestamp
);

CREATE TABLE change_password (
    id_change_password_pfk uuid not null PRIMARY KEY,
    encoded_activation_key char(192) not null check(length(encoded_activation_key) = 192),
    created timestamp with time zone not null default current_timestamp
);

CREATE TABLE change_email (
    id_change_email_pfk uuid not null PRIMARY KEY,
    encoded_email_un varchar(256) not null UNIQUE,
    encoded_activation_key char(192) not null check(length(encoded_activation_key) = 192),
    created timestamp with time zone not null default current_timestamp
);

CREATE TABLE note (
    id_note_pk uuid not null default uuid_generate_v4() PRIMARY KEY,
    id_userprofile_fk uuid not null,
    title varchar(100) not null,
    body text not null,
    priority enum_level not null,
    created timestamp with time zone not null default current_timestamp
);
 
ALTER TABLE verify_email ADD CONSTRAINT constraint_verify_email_userprofile
    FOREIGN KEY (id_verify_email_pfk)
    REFERENCES userprofile (id_userprofile_pk);
 
ALTER TABLE change_password ADD CONSTRAINT constraint_change_password_userprofile
    FOREIGN KEY (id_change_password_pfk)
    REFERENCES userprofile (id_userprofile_pk);
 
ALTER TABLE change_email ADD CONSTRAINT constraint_change_email_userprofile
    FOREIGN KEY (id_change_email_pfk)
    REFERENCES userprofile (id_userprofile_pk);
 
ALTER TABLE note ADD CONSTRAINT constraint_note_userprofile
    FOREIGN KEY (id_userprofile_fk)
    REFERENCES userprofile (id_userprofile_pk);