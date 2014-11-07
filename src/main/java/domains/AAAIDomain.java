package domains;

import java.util.Vector;

import org.metacsp.framework.VariablePrototype;

import pfd0Symbolic.FluentConstraint;
import pfd0Symbolic.FluentNetworkSolver;
import pfd0Symbolic.PFD0Method;
import pfd0Symbolic.PFD0Operator;
import pfd0Symbolic.PFD0Precondition;
import pfd0Symbolic.PlanReportroryItem;
import pfd0Symbolic.FluentConstraint.Type;

public class AAAIDomain {
	
	private static String N = "n";
	
	
	public static String[][] createSymbols() {
		String[][] symbols = new String[8][];
		// predicates  
		// index: 0
		symbols[0] = new String[] {"On", "RobotAt", "Holding", "HasArmPosture", "HasTorsoPosture",
				"Connected",
				// operators
				"!move_base", "!move_base_blind", "!place_object", "!pick_up_object",
				"!move_arm_to_side", "!move_arms_to_carryposture", "!tuck_arms", "!move_torso",
				// methods
				"drive", "assume_default_driving_pose", "assume_manipulation_pose",
				"move_both_arms_to_side", "grasp_object_w_arm", "get_object_w_arm"
				};	
		// race:Kitchenware		
		// index: 1, 2
		symbols[1] = new String[] {"mug1", "mug2", "sugarpot1", "milk1", N};
		// race:PlacingArea
		// index: 3
		symbols[2] = new String[] {"placingAreaEastRightCounter1",
				"placingAreaWestLeftTable1", "placingAreaWestRightTable1",
				"placingAreaEastLeftTable1", "placingAreaEastRightTable1",
				"placingAreaNorthLeftTable2", "placingAreaNorthRightTable2",
				"placingAreaSouthLeftTable2", "placingAreaSouthRightTable2",
				"trayArea1", 
				N};
		// race:ManipulationArea, race:PreManipulationArea, race:FloorArea, race:SittingArea
		// index: 4, 5
		symbols[3] = new String[] {"manipulationAreaEastCounter1", "preManipulationAreaEastCounter1",
				"manipulationAreaNorthTable1", "manipulationAreaSouthTable1",
				"preManipulationAreaNorthTable1", "preManipulationAreaSouthTable1",
				"manipulationAreaWestTable2", "manipulationAreaEastTable2",
				"preManipulationAreaWestTable2", "preManipulationAreaEastTable2",
				"floorAreaTamsRestaurant1", 
				"sittingAreaWestTable1", "sittingAreaEastTable1",
				"sittingAreaNorthTable2", "sittingConstraintSouthTable2",
				N};
		// race:Furniture
		// index: 6 
		symbols[4] = new String[] {"table1", "table2", "counter1", N};
		// race:Guest
		// index: 7
		symbols[5] = new String[] {"guest1", "guest2", N};
		// race:Arm
		// index: 8, 9
		symbols[6] = new String[] {"leftArm1", "rightArm1", N};
		// race:RobotPosture
		// index: 10, 11 
		symbols[7] = new String[] {
				"armTuckedPosture", "armUnTuckedPosture", "armToSidePosture", "armUnnamedPosture", "armCarryPosture",
				"torsoUpPosture", "torsoDownPosture", "torsoMiddlePosture", 
				N};
		return symbols;
	}
	
	public static int[] createIngredients() {
		return new int[] {1,2,1,2,1,1,2,2};
	}
	
	
	public static Vector<PlanReportroryItem> createMethods(FluentNetworkSolver groundSolver) {
		Vector<PlanReportroryItem> ret = new Vector<PlanReportroryItem>();
		
		
		// ###################### DRIVE ##########################################################
		
		VariablePrototype assume_driving_pose = new VariablePrototype(groundSolver, "M", 
				"assume_default_driving_pose", new String[] {N, N, N, N, N, N, N, N, N, N, N});
		VariablePrototype move_base = new VariablePrototype(groundSolver, "M", 
				"!move_base", new String[] {N, N, N, "?toArea", N, N, N, N, N, N, N});
		
		FluentConstraint beforeD1 = new FluentConstraint(FluentConstraint.Type.BEFORE);
		beforeD1.setFrom(assume_driving_pose);
		beforeD1.setTo(move_base);
		ret.add(new PFD0Method("drive", new String[] {N, N, N, "?toArea", N, N, N, N, N, N, N}, 
				new PFD0Precondition[] {}, 
				new VariablePrototype[] {move_base, assume_driving_pose}, 
				new FluentConstraint[] {beforeD1}));
		
	// ###################### ASSUME_DEFAULT_DRIVING_POSE ########################################
		
		VariablePrototype tuck_arms_A1 = new VariablePrototype(groundSolver, "M", 
				"!tuck_arms", new String[] {N, N, N, N, N, N, N, "leftArm1", "rightArm1", "armTuckedPosture", "armTuckedPosture"});
		VariablePrototype move_torso_A1 = new VariablePrototype(groundSolver, "M", 
				"!move_torso", new String[] {N, N, N, N, N, N, N, N, N, "torsoDownPosture", N});

		FluentConstraint beforeA1 = new FluentConstraint(FluentConstraint.Type.BEFORE);
		beforeA1.setFrom(tuck_arms_A1);
		beforeA1.setTo(move_torso_A1);
		ret.add(new PFD0Method("assume_default_driving_pose", new String[] {N, N, N, N, N, N, N, N, N, N, N}, 
				new PFD0Precondition[] {}, 
				new VariablePrototype[] {tuck_arms_A1, move_torso_A1}, 
				new FluentConstraint[] {beforeA1})); // TODO this before is not needed!
		
		// ###################### MOVE_BOTH_ARMS_TO_SIDE_POSE ########################################
		VariablePrototype tuck_arms_A2 = new VariablePrototype(groundSolver, "M", 
				"!tuck_arms", new String[] {N, N, N, N, N, N, N, "leftArm1", "rightArm1", "armUnTuckedPosture", "armUnTuckedPosture"});
		VariablePrototype move_left_to_side_A2 = new VariablePrototype(groundSolver, "M", 
				"!move_arm_to_side", new String[] {N, N, N, N, N, N, N, "leftArm1", N, N, N});
		VariablePrototype move_right_to_side_A2 = new VariablePrototype(groundSolver, "M", 
				"!move_arm_to_side", new String[] {N, N, N, N, N, N, N, "rightArm1", N, N, N});
		
		FluentConstraint beforeA2l = new FluentConstraint(FluentConstraint.Type.BEFORE);
		beforeA2l.setFrom(tuck_arms_A2);
		beforeA2l.setTo(move_left_to_side_A2);
		FluentConstraint beforeA2r = new FluentConstraint(FluentConstraint.Type.BEFORE);
		beforeA2r.setFrom(tuck_arms_A2);
		beforeA2r.setTo(move_right_to_side_A2);
		ret.add(new PFD0Method("move_both_arms_to_side", new String[] {N, N, N, N, N, N, N, N, N, N, N}, 
				new PFD0Precondition[] {}, 
				new VariablePrototype[] {tuck_arms_A2, move_left_to_side_A2, move_right_to_side_A2}, 
				new FluentConstraint[] {beforeA2l, beforeA2r}));

		
	// ###################### ASSUME_MANIPULATION_POSE ########################################
		PFD0Precondition atM1 = new PFD0Precondition("RobotAt", 
				new String[] {N, N, N, "?preArea", N, N, N, N, N, N, N}, 
				new int[] {3, 4});
		PFD0Precondition connectedM1 = new PFD0Precondition("Connected", 
				new String[] {N, N, "?plArea", "?manArea", "?preArea", N, N, N, N, N, N}, 
				new int[] {3, 3, 4, 4});
		
		VariablePrototype move_torso_M1 = new VariablePrototype(groundSolver, "M", 
				"!move_torso", new String[] {N, N, N, N, N, N, N, N, N, "torsoUpPosture", N});
		VariablePrototype move_both_M1 = new VariablePrototype(groundSolver, "M", 
				"move_both_arms_to_side", new String[] {N, N, N, N, N, N, N, N, N, N, N});
		VariablePrototype move_blind_M1 = new VariablePrototype(groundSolver, "M", 
				"!move_base_blind", new String[] {N, N, N, "?manArea", N, N, N, N, N, N, N});

		FluentConstraint beforeM1a = new FluentConstraint(FluentConstraint.Type.BEFORE);
		beforeM1a.setFrom(move_torso_M1);
		beforeM1a.setTo(move_blind_M1);
		FluentConstraint beforeM1b = new FluentConstraint(FluentConstraint.Type.BEFORE);
		beforeM1b.setFrom(move_both_M1);
		beforeM1b.setTo(move_blind_M1);
		
		ret.add(new PFD0Method("assume_manipulation_pose", new String[] {N, N, N, "?manArea", "?preArea", N, N, N, N, N, N}, 
				new PFD0Precondition[] {},//atM1, connectedM1}, 
				new VariablePrototype[] {move_torso_M1, move_both_M1, move_blind_M1}, 
				new FluentConstraint[] {beforeM1a, beforeM1b}));
		
	// ###################### GRASP_OBJECT_W_ARM ########################################
			PFD0Precondition atG1 = new PFD0Precondition("RobotAt", 
					new String[] {N, N, N, "?preArea", N, N, N, N, N, N, N}, 
					new int[] {3, 4});
			PFD0Precondition connectedG1 = new PFD0Precondition("Connected", 
					new String[] {N, N, "?plArea", "?manArea", "?preArea", N, N, N, N, N, N}, 
					new int[] {2,2, 3,3, 4,4});
			PFD0Precondition onG1 = new PFD0Precondition("On", 
					new String[] {"?obj", N, "?plArea", N, N, N, N, N, N, N, N}, 
					new int[] {0,0, 2,2});
			
			VariablePrototype assume_G1 = new VariablePrototype(groundSolver, "M", 
					"assume_manipulation_pose", new String[] {N, N, N, "?manArea", "?preArea", N, N, N, N, N, N});
			VariablePrototype pick_G1 = new VariablePrototype(groundSolver, "M", 
					"!pick_up_object", new String[] {"?obj", N, "?plArea", "?manArea", N, N, N, "?arm", N, N, N});
			VariablePrototype move_blind_G1 = new VariablePrototype(groundSolver, "M", 
					"!move_base_blind", new String[] {N, N, N, "?preArea", N, N, N, N, N, N, N});

			FluentConstraint beforeG1a = new FluentConstraint(FluentConstraint.Type.BEFORE);
			beforeG1a.setFrom(assume_G1);
			beforeG1a.setTo(pick_G1);
			FluentConstraint beforeG1b = new FluentConstraint(FluentConstraint.Type.BEFORE);
			beforeG1b.setFrom(pick_G1);
			beforeG1b.setTo(move_blind_G1);
			
			ret.add(new PFD0Method("grasp_object_w_arm", 
					new String[] {"?obj", N, "?plArea", "?manArea", "?preArea", N, N, "?arm", N, N, N}, 
					new PFD0Precondition[] {atG1, connectedG1, onG1}, 
					new VariablePrototype[] {assume_G1, pick_G1, move_blind_G1}, 
					new FluentConstraint[] {beforeG1a, beforeG1b}
			));
			
		// ###################### GET_OBJECT_W_ARM ########################################
			// TODO check if driving is necessary					
//			PFD0Precondition atG2 = new PFD0Precondition("RobotAt", 
//					new String[] {N, N, N, "?preArea", N, N, N, N, N, N, N}, 
//					new int[] {3, 4});
			PFD0Precondition onG2 = new PFD0Precondition("On", 
					new String[] {"?obj", N, "?plArea", N, N, N, N, N, N, N, N}, 
					new int[] {0,0, 2,2});
			PFD0Precondition connectedG2 = new PFD0Precondition("Connected", 
					new String[] {N, N, "?plArea", "?manArea", "?preArea", N, N, N, N, N, N}, 
					new int[] {2,2, 4,4});

			VariablePrototype drive_G2 = new VariablePrototype(groundSolver, "M", 
					"drive", new String[] {N, N, N, "?preArea", N, N, N, N, N, N, N});
			VariablePrototype grasp_G2 = new VariablePrototype(groundSolver, "M", 
					"grasp_object_w_arm", new String[] {"?obj", N, "?plArea", "?manArea", "?preArea", N, N, "?arm", N, N, N});

			FluentConstraint beforeG2 = new FluentConstraint(FluentConstraint.Type.BEFORE);
			beforeG2.setFrom(drive_G2);
			beforeG2.setTo(grasp_G2);

			ret.add(new PFD0Method("get_object_w_arm", 
					new String[] {"?obj", N, "?plArea", N, "?preArea", N, N, "?arm", N, N, N}, 
					new PFD0Precondition[] {onG2, connectedG2}, 
					new VariablePrototype[] {drive_G2, grasp_G2},
					new FluentConstraint[] {beforeG2}
					));
		
		// #########
		return ret;
	}
	
	
	/**
	 * Create all operators.
	 * In the old domain we have the following operators:
	 * * !tuck_arms ?leftarmgoal ?rightarmgoal
	 * * !move_arms_to_carryposture
	 * * !move_arm_to_side ?whicharm
	 * * !pick_up_object ?object ?arm
	 * * !place_object ?object ?arm ?placingArea
	 * * !move_base ?to
	 * * !move_base_blind ?to
	 * * !move_torso ?torsoPostureType
	 * @param groundSolver 
	 * @return The operators.
	 */
	public static Vector<PlanReportroryItem> createOperators(FluentNetworkSolver groundSolver) {
		Vector<PlanReportroryItem> ret = new Vector<PlanReportroryItem>();
		
		//###################### !tuck_arms ?leftarmgoal ?rightarmgoal ##############################
		PFD0Precondition oldArmPosture3 = new PFD0Precondition("HasArmPosture", 
				new String[] {N, N, N, N, N, N, N, "leftArm1", N, "?oldPosture1", N}, 
				new int[] {7,7});
		oldArmPosture3.setNegativeEffect(true);
		PFD0Precondition oldArmPosture4 = new PFD0Precondition("HasArmPosture", 
				new String[] {N, N, N, N, N, N, N, "rightArm1", N, "?oldPosture2", N}, 
				new int[] {7, 8});
		oldArmPosture4.setNegativeEffect(true);
		
		// TODO make sure that the goal is in {ArmTuckedPosture, ArmUnTuckedPosture
		VariablePrototype newArmPosture3 = new VariablePrototype(groundSolver, "S", 
				"HasArmPosture", new String[] {N, N, N, N, N, N, N, "leftArm1", N, "?leftGoal", N});
		VariablePrototype newArmPosture4 = new VariablePrototype(groundSolver, "S", 
				"HasArmPosture", new String[] {N, N, N, N, N, N, N, "rightArm1", N, "?rightGoal", N});

		ret.add( new PFD0Operator("!tuck_arms", 
				new String[] {N, N, N, N, N, N, N, "leftArm1", "rightArm1", "?leftGoal", "?rightGoal"},
				new PFD0Precondition[]{oldArmPosture3, oldArmPosture4}, 
				new VariablePrototype[] {newArmPosture3, newArmPosture4}));
		
		
		//################ !move_arms_to_carryposture ################################################
		// TODO make sure that torso is down
		PFD0Precondition oldArmPosture1 = new PFD0Precondition("HasArmPosture", 
				new String[] {N, N, N, N, N, N, N, "leftArm1", N, "?oldPosture1", N}, 
				new int[] {7,7});
		oldArmPosture1.setNegativeEffect(true);
		PFD0Precondition oldArmPosture2 = new PFD0Precondition("HasArmPosture", 
				new String[] {N, N, N, N, N, N, N, "rightArm1", N, "?oldPosture2", N}, 
				new int[] {7, 8});
		oldArmPosture2.setNegativeEffect(true);
		
		VariablePrototype newArmPosture1 = new VariablePrototype(groundSolver, "S", 
				"HasArmPosture", new String[] {N, N, N, N, N, N, N, "leftArm1", N, "armCarryPosture", N});
		VariablePrototype newArmPosture2 = new VariablePrototype(groundSolver, "S", 
				"HasArmPosture", new String[] {N, N, N, N, N, N, N, "rightArm1", N, "armCarryPosture", N});

		ret.add( new PFD0Operator("!move_arms_to_carryposture", 
				new String[] {N, N, N, N, N, N, N, "leftArm1", "rightArm1", N, N},
				new PFD0Precondition[]{oldArmPosture1, oldArmPosture2}, 
				new VariablePrototype[] {newArmPosture1, newArmPosture2}));
		
		//################### !move_arm_to_side ?whicharm ###########################################
			// hasArmPosture ?arm ?armposture
		// TODO: here we could restrict the domain of the old posture to be not tucked
		// + same for the other arm
		PFD0Precondition oldArmPosture0 = new PFD0Precondition("HasArmPosture", 
				new String[] {N, N, N, N, N, N, N, "?arm", N, "?oldPosture", N}, 
				new int[] {7,7});
		oldArmPosture0.setNegativeEffect(true);
		
		VariablePrototype newArmPosture0 = new VariablePrototype(groundSolver, "S", 
				"HasArmPosture", new String[] {N, N, N, N, N, N, N, "?arm", N, "armToSidePosture", N});

		ret.add( new PFD0Operator("!move_arm_to_side", 
				new String[] {N, N, N, N, N, N, N, "?arm", N, N, N},
				new PFD0Precondition[]{oldArmPosture0}, 
				new VariablePrototype[] {newArmPosture0}));


		//################ !pick_up_object ?object ?arm #############################################
		// TODO: NOT HOLDING
		PFD0Precondition onPrePickObj = new PFD0Precondition("On", 
				new String[] {"?obj", N, "?fromArea", N, N, N, N, N, N, N, N}, 
				new int[] {0,0, 2,2});
		onPrePickObj.setNegativeEffect(true);
		PFD0Precondition atPrePickObj = new PFD0Precondition("RobotAt", 
				new String[] {N, N, N, "?mArea", N, N, N, N, N, N, N}, 
				new int[] {3, 3});
		PFD0Precondition connectedPickObj = new PFD0Precondition("Connected", 
				new String[] {N, N, "?fromArea", "?mArea", "?preArea", N, N, N, N, N, N}, 
				new int[] {2,2, 3, 3});
		
		VariablePrototype holdingEffPickObj = new VariablePrototype(groundSolver, "S", 
				"Holding", new String[] {"?obj", N, N, N, N, N, N, "?arm", N, N, N});

		// TODO delete old armposture and create new
		ret.add( new PFD0Operator("!pick_up_object", 
				new String[] {"?obj", N, "?fromArea", "?mArea?", N, N, N, "?arm", N, N, N}, // head
				new PFD0Precondition[]{onPrePickObj, atPrePickObj, connectedPickObj}, 
				new VariablePrototype[] {holdingEffPickObj}));
			
		
		//############### !place_object ?object ?arm ?placingArea ###################
		PFD0Precondition holdingPrePlaceObj = new PFD0Precondition("Holding", 
				new String[] {"?obj", N, N, N, N, N, N, "?arm", N, N, N}, 
				new int[] {0,0, 7,7});
		holdingPrePlaceObj.setNegativeEffect(true);
		PFD0Precondition atPrePlaceObj = new PFD0Precondition("RobotAt", 
				new String[] {N, N, N, "?mArea", N, N, N, N, N, N, N}, 
				new int[] {3, 3});
		PFD0Precondition connectedPlaceObj = new PFD0Precondition("Connected", 
				new String[] {N, N, "?toArea", "?mArea", "?preArea", N, N, N, N, N, N}, 
				new int[] {2,2, 3, 3});
		
		VariablePrototype onEffPlaceObj = new VariablePrototype(groundSolver, "S", 
				"On", new String[] {"?obj", N, "?toArea", N, N, N, N, N, N, N, N});

		// TODO delete old armposture and create new
		ret.add( new PFD0Operator("!place_object", 
				new String[] {"?obj", N, "?toArea", "?mArea?", N, N, N, "?arm", N, N, N}, // head
				new PFD0Precondition[]{holdingPrePlaceObj, atPrePlaceObj, connectedPlaceObj}, 
				new VariablePrototype[] {onEffPlaceObj}));
		
		//#################### !move_base ?to #########################################
		PFD0Precondition atPreMB = new PFD0Precondition("RobotAt", 
				new String[] {N, N, N, "?formArea", N, N, N, N, N, N, N}, 
				new int[] {});
		atPreMB.setNegativeEffect(true);
		
		VariablePrototype robotatEffMB = new VariablePrototype(groundSolver, "S", 
				"RobotAt", new String[] {N, N, N, "?toArea", N, N, N, N, N, N, N});
		
		ret.add( new PFD0Operator("!move_base", 
				new String[] {N, N, N, "?toArea", N, N, N, N, N, N, N}, // head
				new PFD0Precondition[]{atPreMB}, 
				new VariablePrototype[] {robotatEffMB}));
		
		//#################### !move_base_blind ?to ###################################
		PFD0Precondition atPreMBB = new PFD0Precondition("RobotAt", 
				new String[] {N, N, N, "?formArea", N, N, N, N, N, N, N}, 
				new int[] {});
		atPreMBB.setNegativeEffect(true);
		
		// TODO: check if they are connected
		// this is only possible if we can have binding constraints between preconditions and effects
		
		VariablePrototype robotatEffMBB = new VariablePrototype(groundSolver, "S", 
				"RobotAt", new String[] {N, N, N, "?toArea", N, N, N, N, N, N, N});
		
		ret.add( new PFD0Operator("!move_base_blind", 
				new String[] {N, N, N, "?toArea", N, N, N, N, N, N, N}, // head
				new PFD0Precondition[]{atPreMBB}, 
				new VariablePrototype[] {robotatEffMBB}));
		
		
		//################# !move_torso ?torsoPostureType ###########################################
		PFD0Precondition oldTorsoPosture0 = new PFD0Precondition("HasTorsoPosture", 
				new String[] {N, N, N, N, N, N, N, N, N, "?oldPosture", N}, 
				new int[] {});
		oldTorsoPosture0.setNegativeEffect(true);
		
		VariablePrototype newTorsoPosture0 = new VariablePrototype(groundSolver, "S", 
				"HasTorsoPosture", new String[] {N, N, N, N, N, N, N, N, N, "?newPosture", N});

		ret.add( new PFD0Operator("!move_torso", 
				new String[] {N, N, N, N, N, N, N, N , N, "?newPosture", N},
				new PFD0Precondition[]{oldTorsoPosture0}, 
				new VariablePrototype[] {newTorsoPosture0}));
		
		return ret;
		
		
	}
	
	

}