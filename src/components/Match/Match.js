import React from 'react';

import { MdWarning } from 'react-icons/md';
import { getColor } from 'assets/rainbow';

import MatchAccent from './MatchAccent';
import styles from './Match.module.css';

const Match = ({ value, className, accent }) => {
	const prettyValue = value ? value : 'No Value';
	return (
		<span
			title={prettyValue}
			style={{ color: value ? getColor(accent).dark : null }}
			data-missing={!value}
			className={[styles.base, className].join(' ')}
		>
			<div className={styles.icon}>
				{value ? (
					accent !== undefined && <MatchAccent accent={accent} />
				) : (
					<MdWarning />
				)}
			</div>
			<span className={styles.value}>{prettyValue}</span>
		</span>
	);
};

export default Match;
