import React from 'react';

import styles from './CenteredPanel.module.css';

const CenteredPanel = ({ children }) => (
	<div className={styles.root}>{children}</div>
);

export default CenteredPanel;
