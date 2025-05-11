CREATE TABLE otp_codes (
   id            SERIAL PRIMARY KEY,
   user_id       INT NOT NULL
       REFERENCES users(id)
           ON DELETE CASCADE,
   operation_id  VARCHAR(100),
   code          VARCHAR(20) NOT NULL,
   status        VARCHAR(10) NOT NULL
       CHECK (status IN ('ACTIVE','EXPIRED','USED')),
   created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
   expires_at    TIMESTAMP WITH TIME ZONE NOT NULL
);
