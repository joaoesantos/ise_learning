------------------------------------
--         REBUILD MODEL          --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_rebuildmodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;
	CALL p_dropmodel();
	CALL p_createmodel();
	CALL p_fillmodel();
END;
$BODY$;