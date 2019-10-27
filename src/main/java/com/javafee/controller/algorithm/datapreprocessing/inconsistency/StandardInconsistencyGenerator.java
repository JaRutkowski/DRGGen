package com.javafee.controller.algorithm.datapreprocessing.inconsistency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.javafee.controller.algorithm.exception.AlgorithmException;
import com.javafee.controller.algorithm.process.InconsistencyProcess;
import com.javafee.controller.algorithm.process.VectorProcess;
import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.params.Params;

@Named("StandardInconsistencyGenerator")
public class StandardInconsistencyGenerator implements InconsistencyGenerator {
	private boolean recursiveRemoval = false;
	private List<Long> allAttributesForRecursive = null;

	@Override
	public Vector<Vector> generate(Vector<Vector> data) throws AlgorithmException {
		// Check if data is consistent or not
		if (!InconsistencyProcess.checkIfInconsistencyExists(data)) {
			// Count max occurrences of each types of conditional values
			Map<Long, Long> maxOccurrencesOfEachTypesOfValuesForEachConditionalAttributes =
					VectorProcess.countMaxOccurrencesOfEachTypesOfValuesForEachConditionalAttributesExceptDecisionValues(data);

			// Find max value of occurrences
			Long maxCountForValue = Collections.max(maxOccurrencesOfEachTypesOfValuesForEachConditionalAttributes.entrySet(),
					Map.Entry.comparingByValue()).getValue();

			// Find keys - attributes for found max value of occurrences
			List<Long> attributesIndexes = maxOccurrencesOfEachTypesOfValuesForEachConditionalAttributes.entrySet().stream()
					.filter(entry -> maxCountForValue.equals(entry.getValue()))
					.map(Map.Entry::getKey).collect(Collectors.toList());

			long[] attributes;
			if (SystemProperties.isSystemParameterRemoveFirstAttributeInconsistencyGenerator()) {
				attributes = new long[]{attributesIndexes.stream().findFirst().get()};
				// Remove one attribute which the lowest index
				removeAttributesByIndexes(data, attributes);
			} else {
				attributes = attributesIndexes.stream().mapToLong(x -> x).toArray();
				// Remove all attributes
				removeAttributesByIndexes(data, attributes);
			}

			// Recursive attributes removal
			if (SystemProperties.isSystemParameterRecursiveRemovalInconsistencyGenerator()) {
				if (!recursiveRemoval)
					this.allAttributesForRecursive = initializeAllRemovedAttributesListForRecursiveBaseOnDataSize(data.get(0).size());
				allAttributesForRecursive = setToNullRemovedAttributesForRecursive(this.allAttributesForRecursive, Arrays.stream(attributes).boxed().collect(Collectors.toList()));
			}

			// Refresh decision attribute index after removal
			Common.refreshSystemParameterDecisionAttributeIndexAfterAttributesRemoval(attributes);

			// Recursive attributes removal, with stop condition (inconsistency generation)
			if (SystemProperties.isSystemParameterRecursiveRemovalInconsistencyGenerator() &&
					!InconsistencyProcess.checkIfInconsistencyExists(data)) {
				recursiveRemoval = true;
				generate(data);
			}

			// Store removed attributes indexes in params
			Params.getInstance().add("REMOVED_ATTRIBUTE_INDEXES",
					SystemProperties.isSystemParameterRecursiveRemovalInconsistencyGenerator() ?
							getRemovedElementIndexesFromAllAttributesForRecursive(allAttributesForRecursive) : attributes);
		} else {
			if (!this.recursiveRemoval)
				throw new AlgorithmException(
						SystemProperties.getResourceBundle().getString("optionPane.standardInconsistencyGenerator.dataConsistencyValidationMessage"));
		}
		recursiveRemoval = false;
		return data;
	}

	private Vector<Vector> removeAttributesByIndexes(Vector<Vector> data, long[] attributes) {
		for (Vector row : data) {
			List<Long> attributesList = Arrays.stream(attributes).boxed().collect(Collectors.toList());
			while (!attributesList.isEmpty()) {
				long attributeWithMaxIndexToRemove = Collections.max(attributesList);
				row.remove(Math.toIntExact(attributeWithMaxIndexToRemove));
				attributesList.remove(attributeWithMaxIndexToRemove);
			}
		}
		return data;
	}

	private List<Long> initializeAllRemovedAttributesListForRecursiveBaseOnDataSize(int dataSize) {
		List<Long> allAttributesForRecursive = new ArrayList<>();
		for (int index = 0; index < dataSize; index++)
			allAttributesForRecursive.add(new Long(index));
		return allAttributesForRecursive;
	}

	private List<Long> setToNullRemovedAttributesForRecursive(List<Long> allAttributesForRecursive, List<Long> currentRemovedAttributes) {
		//		for (Long removedAttribute : currentRemovedAttributes) {
		//			int notNullElementIndex = 0, allAttributesForRecursiveIndex = 0;
		//			for (Long attribute : allAttributesForRecursive) {
		//				if (attribute != null) {
		//					if (removedAttribute == notNullElementIndex)
		//						allAttributesForRecursive.set(allAttributesForRecursiveIndex, null);
		//					notNullElementIndex++; // check
		//				}
		//				allAttributesForRecursiveIndex++;
		//			}
		//		}

		while (!currentRemovedAttributes.isEmpty()) {
			long attributeWithMaxIndexToRemove = Collections.max(currentRemovedAttributes);
			int notNullElementIndex = 0, allAttributesForRecursiveIndex = 0;
			for (Long attribute : allAttributesForRecursive) {
				if (attribute != null) {
					if (attributeWithMaxIndexToRemove == notNullElementIndex)
						allAttributesForRecursive.set(allAttributesForRecursiveIndex, null);
					notNullElementIndex++; // check
				}
				allAttributesForRecursiveIndex++;
			}
			currentRemovedAttributes.remove(attributeWithMaxIndexToRemove);
		}

		//		for (Vector row : data) {
		//			List<Long> attributesList = Arrays.stream(attributes).boxed().collect(Collectors.toList());
		//			while (!attributesList.isEmpty()) {
		//				long attributeWithMaxIndexToRemove = Collections.max(attributesList);
		//				row.remove(Math.toIntExact(attributeWithMaxIndexToRemove));
		//				attributesList.remove(attributeWithMaxIndexToRemove);
		//			}
		//		}
		return allAttributesForRecursive;
	}

	private long[] getRemovedElementIndexesFromAllAttributesForRecursive(List<Long> allAttributesForRecursive) {
		long numberOfNullElements = allAttributesForRecursive.stream().filter(element -> element == null).count();
		long[] removedElementIndexes = new long[Math.toIntExact(numberOfNullElements)];

		int removedElementIndex = 0;
		for (int index = 0; index < allAttributesForRecursive.size(); index++) {
			if (allAttributesForRecursive.get(index) == null) {
				removedElementIndexes[removedElementIndex] = index;
				removedElementIndex++;
			}
		}

		return removedElementIndexes;
	}
}
