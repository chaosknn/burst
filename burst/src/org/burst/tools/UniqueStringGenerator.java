package org.burst.tools;

public class UniqueStringGenerator

{
	private static final int MAX_GENERATE_COUNT = 99999;

	private static int generateCount = 0;

	private UniqueStringGenerator() {
	}

	public static synchronized String getUniqueString()

	{

		if (generateCount > MAX_GENERATE_COUNT){
			generateCount = 0;
		}

		String uniqueNumber = Long.toString(System.currentTimeMillis())
				+ Integer.toString(generateCount);

		generateCount++;

		return uniqueNumber;

	}

}
