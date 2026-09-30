BEGIN;

DO $$
DECLARE
    global_cpf_constraint text;
BEGIN
    SELECT constraint_info.conname
    INTO global_cpf_constraint
    FROM pg_constraint AS constraint_info
    WHERE constraint_info.conrelid = 'tb_participants'::regclass
      AND constraint_info.contype = 'u'
      AND array_length(constraint_info.conkey, 1) = 1
      AND (
          SELECT column_info.attname
          FROM pg_attribute AS column_info
          WHERE column_info.attrelid = constraint_info.conrelid
            AND column_info.attnum = constraint_info.conkey[1]
      ) = 'cpf'
    LIMIT 1;

    IF global_cpf_constraint IS NOT NULL THEN
        EXECUTE format(
            'ALTER TABLE tb_participants DROP CONSTRAINT %I',
            global_cpf_constraint
        );
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conrelid = 'tb_participants'::regclass
          AND conname = 'uk_participant_institution_cpf'
    ) THEN
        ALTER TABLE tb_participants
            ADD CONSTRAINT uk_participant_institution_cpf
            UNIQUE (institution_id, cpf);
    END IF;
END $$;

COMMIT;