import React from 'react';
import { MdSync, MdCloud } from 'react-icons/md';

import styles from './SyncSpinner.module.css';

const SyncSpinner = ({ type }) => {
	return (
		<div className={styles.wrapper}>
			{type === 'cloud' && <MdCloud className={styles.cloud} />}
			<div className={styles.overlap}>
				<div
					className={styles.spinner}
					data-cloud={type === 'cloud' ? true : null}
				>
					<MdSync />
				</div>
			</div>
		</div>
	);
};

export default SyncSpinner;
