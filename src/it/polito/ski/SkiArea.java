package it.polito.ski;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SkiArea {

	public class Slope {
		String name, difficulty, liftName;

		public Slope(String name, String difficulty, String liftName) {
			this.name = name;
			this.difficulty = difficulty;
			this.liftName = liftName;
		}

		public String getName() {
			return name;
		}

		public String getDifficulty() {
			return difficulty;
		}

		public String getLiftName() {
			return liftName;
		}
		
	}

	public class Parking {
		String name;
		int slots;


		public Parking(String name, int slots) {
			this.name = name;
			this.slots = slots;
		}
		public String getName() {
			return name;
		}
		public int getSlots() {
			return slots;
		}
	
		
	}

	public class Lift {
		String name;
		LiftType type;
		public Lift(String name, LiftType type) {
			this.name = name;
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public LiftType getType() {
			return type;
		}
		
	}
	
	private String name;
	private final Map<String, LiftType> liftTypes = new TreeMap<>();
	private final Map<String, Lift> lifts = new TreeMap<>();
	private final Map<String, Slope> slopes = new TreeMap<>();
	private final Map<String, Parking> parkings = new TreeMap<>();
	private final Map<Parking, List<Lift>> parkLift = new HashMap<>();

	/**
	 * Creates a new ski area
	 * @param name name of the new ski area
	 */
	public SkiArea(String name) {
		this.name = name;
    }

	/**
	 * Retrieves the name of the ski area
	 * @return name
	 */
	public String getName() { return this.name; }

    /**
     * define a new lift type providing the code, the category (Cable Cabin, Chair, Ski-lift)
     * and the capacity (number of skiers carried) of a single unit
     * 
     * @param code		name of the new type
     * @param category	category of the lift
     * @param capacity	number of skiers per unit
     * @throws InvalidLiftException in case of duplicate code or if the capacity is <= 0
     */
    public void liftType(String code, String category, int capacity) throws InvalidLiftException {
		if (liftTypes.containsKey(code) || capacity <= 0)
			throw new InvalidLiftException();
		liftTypes.put(code, new LiftType(code, category, capacity));
	}
    
    /**
     * retrieves the category of a given lift type code
     * @param typeCode lift type code
     * @return the category of the type
     * @throws InvalidLiftException if the code has not been defined
     */
    public String getCategory(String typeCode) throws InvalidLiftException {
		if (!liftTypes.containsKey(typeCode))
			throw new InvalidLiftException();
		return liftTypes.get(typeCode).getCategory();
    }

    /**
     * retrieves the capacity of a given lift type code
     * @param typeCode lift type code
     * @return the capacity of the type
     * @throws InvalidLiftException if the code has not been defined
     */
    public int getCapacity(String typeCode) throws InvalidLiftException {
		if (!liftTypes.containsKey(typeCode))
			throw new InvalidLiftException();
		return liftTypes.get(typeCode).getCapacity();
    }


    /**
     * retrieves the list of lift types
     * @return the list of codes
     */
	public Collection<String> types() {
		return liftTypes.keySet();
	}
	
	/**
	 * Creates new lift with given name and type
	 * 
	 * @param name		name of the new lift
	 * @param typeCode	type of the lift
	 * @throws InvalidLiftException in case the lift type is not defined
	 */
    public void createLift(String name, String typeCode) throws InvalidLiftException{
		if (!liftTypes.containsKey(typeCode))
			throw new InvalidLiftException();
		lifts.put(name, new Lift(name, liftTypes.get(typeCode)));
    }
    
	/**
	 * Retrieves the type of the given lift
	 * @param lift 	name of the lift
	 * @return type of the lift
	 */
	public String getType(String lift) {
		return lifts.get(lift).getType().getCode();
	}

	/**
	 * retrieves the list of lifts defined in the ski area
	 * @return the list of names sorted alphabetically
	 */
	public List<String> getLifts(){
		return new ArrayList<>(lifts.keySet());
    }

	/**
	 * create a new slope with a given name, difficulty and a starting lift
	 * @param name			name of the slope
	 * @param difficulty	difficulty
	 * @param lift			the starting lift for the slope
	 * @throws InvalidLiftException in case the lift has not been defined
	 */
    public void createSlope(String name, String difficulty, String lift) throws InvalidLiftException {
		if (!lifts.containsKey(lift))
			throw new InvalidLiftException();
		slopes.put(name, new Slope(name, difficulty, lift));
    }
    
    /**
     * retrieves the name of the slope
     * @param slopeName name of the slope
     * @return difficulty
     */
	public String getDifficulty(String slopeName) {
		return slopes.get(slopeName).getDifficulty();
	}

	/**
	 * retrieves the start lift
	 * @param slopeName name of the slope
	 * @return starting lift
	 */
	public String getStartLift(String slopeName) {
		return slopes.get(slopeName).getLiftName();
	}

	/**
	 * retrieves the list of defined slopes
	 * 
	 * @return list of slopes
	 */
    public Collection<String> getSlopes(){
		return slopes.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Retrieves the list of slopes starting from a given lift
     * 
     * @param lift the starting lift
     * @return the list of slopes
     */
    public Collection<String> getSlopesFrom(String lift){
		return slopes.entrySet().stream()
			.filter(entry -> entry.getValue().getLiftName().equals(lift))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
    }

    /**
     * Create a new parking with a given number of slots
     * @param name 	new parking name
     * @param slots	slots available in the parking
     */
    public void createParking(String name, int slots){
		Parking temp = new Parking(name, slots);
		parkings.put(name, temp);
		parkLift.put(temp, new ArrayList<>());
    }

    /**
     * Retrieves the number of parking slots available in a given parking
     * @param parking	parking name
     * @return number of slots
     */
	public int getParkingSlots(String parking) {
		return parkings.get(parking).getSlots();
	}

	/**
	 * Define a lift as served by a given parking
	 * @param lift		lift name
	 * @param parking	parking name
	 */
	public void liftServedByParking(String lift, String parking) {
		parkLift.get(parkings.get(parking)).add(lifts.get(lift));
	}

	
	/**
	 * Retrieves the list of lifts served by a parking.
	 * @param parking	parking name
	 * @return the list of lifts
	 */
	public Collection<String> servedLifts(String parking) {
		return parkLift.get(parkings.get(parking)).stream().map(val -> val.getName()).collect(Collectors.toList());
	}

	/**
	 * Checks whether the parking is proportional to the capacity of the lift it is serving.
	 * A parking is considered proportionate if its size divided by the sum of the capacity of the lifts 
	 * served by the parking is less than 30.
	 * 
	 * @param parkingName name of the parking to check
	 * @return true if the parking is proportionate
	 */
	public boolean isParkingProportionate(String parkingName) {
		int size = parkings.get(parkingName).getSlots();
		int totalCapacity = parkLift.get(parkings.get(parkingName)).stream().collect(Collectors.summingInt(val -> val.getType().getCapacity()));
		if (size/totalCapacity < 30)
			return true;
		return false;
	}

	/**
	 * reads the description of lift types and lift descriptions from a text file. 
	 * The contains a description per line. 
	 * Each line starts with a letter indicating the kind of information: "T" stands for Lift Type, 
	 * while "L" stands for Lift.
	 * A lift type is described by code, category and seat number. 
	 * A lift is described by the name and the lift type.
	 * Different data on a line are separated by ";" and possible spaces surrounding the separator are ignored.
	 * If a line contains the wrong number of information it should be skipped and
	 * the method should continue reading the following lines. 
	 * 
	 * @param path 	the path of the file
	 * @throws IOException	in case IO error
	 * @throws InvalidLiftException in case of duplicate type or non-existent lift type
	 */
    public void readLifts(String path) throws IOException, InvalidLiftException {
		Collection<String> content; 
		try (BufferedReader in = new BufferedReader(new FileReader(path))) {
			content = in.lines().collect(Collectors.toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new IOException();
		}
		for (String temp: content) {
			if (temp == null) { throw new IOException(); }
		
			String[] check = temp.toString().split(";");
			if ((check[0].equals("T") && check.length < 4) ||
				(check[0].equals("L") && check.length < 3))
					{ continue; }
			else {
				if (check[0].strip().equals("T")) {
					liftType(check[1].strip(), check[2].strip(), Integer.valueOf(check[3].strip()));
				}
				else if (check[0].equals("L")) {
					createLift(check[1].strip(), check[2].strip());
				}
				else
					continue;
			}

		}
    }

}
