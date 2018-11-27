import React from 'react';

import styles from './MatchAccent.module.css';
import { getColor, getLetter } from 'assets/rainbow';

const MatchAccent = ({ accent }) => (
	<span
		style={{ backgroundColor: getColor(accent).light }}
		className={styles.accent}
	>
		{getLetter(accent)}
	</span>
);

export default MatchAccent;
