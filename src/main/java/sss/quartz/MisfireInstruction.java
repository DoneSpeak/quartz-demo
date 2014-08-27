package sss.quartz;

/**
 * Instructions to the scheduler on how to handler trigger misfires. A trigger
 * misfire occurs when, for some reason, the trigger isn't allows to run when it
 * is scheduled to.
 * 
 * @author jsteele
 */
public enum MisfireInstruction {
	/** */
	DO_NOTHING,
	/** */
	FIRE_AND_PROCEED,
	/** */
	IGNORE_MISFIRES;
}
