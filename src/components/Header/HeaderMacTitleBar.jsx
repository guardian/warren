import React from 'react';

import styles from './HeaderMacTitleBar.module.css';

export default () => {
	return (
		<div
			style={{
				height: [window.screen.getPrimaryDisplay().workArea.y || 30, 'px'].join(
					''
				),
			}}
			className={styles.root}
		/>
	);
};
