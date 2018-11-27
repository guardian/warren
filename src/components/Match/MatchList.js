import React from 'react';

import Match from './Match';
import styles from './MatchList.module.css';

const SampleRow = ({
	sample,
	selected,
	onSelectSample,
	setIndex,
	showAllValues,
	sampleIndex,
	accents,
}) => {
	const clazz = selected ? styles.rowSelected : styles.row;

	const indexedValues = sample.sampledValues.map((value, index) => ({
		value,
		index,
	}));
	const values = showAllValues
		? indexedValues
		: [indexedValues.find(_ => _.value !== '') || { val: '', index: 0 }];

	return (
		<div
			key={sample.name}
			className={clazz}
			onClick={() => onSelectSample(setIndex, sampleIndex)}
		>
			<span className={styles.fileName}>{sample.name}</span>
			{values.map(({ value, index }) => (
				<Match
					className={styles.match}
					value={value}
					accent={accents[index]}
					key={sample.name + index}
				/>
			))}
		</div>
	);
};

const MatchList = ({
	sampleSet,
	currentSample,
	rules,
	showAllValues,
	isCurrentSet,
	onSelectSample,
	setIndex,
}) =>
	sampleSet.samples.length > 0 ? (
		sampleSet.samples.map((sample, sampleIndex) => (
			<SampleRow
				key={sample.name}
				sample={sample}
				showAllValues={showAllValues}
				accents={rules.map(_ => _.accent)}
				selected={isCurrentSet && sampleIndex === currentSample.sample}
				onSelectSample={onSelectSample}
				setIndex={setIndex}
				sampleIndex={sampleIndex}
			/>
		))
	) : (
		<span className={styles.noMatch}>No matches</span>
	);

export default MatchList;
