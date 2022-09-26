CREATE TABLE IF NOT EXISTS groups
(
    id                  bigint PRIMARY KEY,
    name                text,
    coordinates         text,
    creationDate        text,
    studentsCount       int,
    transferredStudents bigint,
    formOfEducation     text,
    semesterEnum        text,
    groupAdmin          text,
    author              bigint
);

CREATE SEQUENCE IF NOT EXISTS groups_ids;

INSERT INTO groups
VALUES (nextval('groups_ids'), 'P32111', '{"x":100,"y":200.0}', '15.09.2022', 42, 20, 'DISTANCE_EDUCATION', 'SECOND',
        '{"name":"Alex","birthday":"20.01.2004","eyeColor":"RED","nationality":"RUSSIA","location":{"x":10.0,"y":10.0,"z":20,"name":"Berlin"}}', 1)