/***
 * Feedback Truvag
 * 
 ***/

IF COL_LENGTH('[dbo].[Employee]', 'empMarriedSince') IS NULL
BEGIN
    ALTER TABLE [dbo].[Employee]
    ADD 
	empMarriedSince   date NULL,
	empDivorcedSince  date NULL,
END
