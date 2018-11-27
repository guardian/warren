import React from 'react';

import styles from './Form.module.css';

const Form = ({ onSubmit, title, children }) => {
	const _onSubmit = e => {
		e.preventDefault();
		onSubmit(e);
	};

	return (
		<form className={styles.root} onSubmit={_onSubmit}>
			<h1>{title}</h1>
			<div className={styles.content}>{children}</div>
		</form>
	);
};

export default Form;
