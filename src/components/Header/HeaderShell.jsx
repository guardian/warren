import React from 'react';
import Header from './Header';

import styles from './HeaderShell.module.css';

export default props => {
	return (
		<div className={styles.root}>
			<Header {...{ ...props, className: null }} />
			<div className={[styles.content, props.className].join(' ')}>
				{props.children}
			</div>
		</div>
	);
};
