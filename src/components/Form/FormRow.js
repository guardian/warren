import React from 'react';

import styles from './Form.module.css';

const FormRow = ({ title, children, htmlFor }) => {
	return (
		<div className={styles.wrap}>
			{title && (
				<label className={styles.label} htmlFor={htmlFor}>
					{title}
				</label>
			)}
			<div className={styles.children}>{children}</div>
		</div>
	);
};

export default FormRow;
