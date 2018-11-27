import React from 'react';
import Button from 'components/Button/Button';

import styles from './WithExpander.module.css';

const DefaultHeader = ({ onToggle, isOpen }) => (
	<header>
		<Button onClick={() => onToggle()}>{isOpen ? 'contract' : 'expand'}</Button>
	</header>
);

const WithExpander = ({
	isOpen,
	onToggle,
	children,
	Header = DefaultHeader,
} = {}) => {
	return (
		<div>
			<Header isOpen={isOpen} onToggle={onToggle} />
			<div className={isOpen ? styles.open : styles.closed}>{children}</div>
		</div>
	);
};

export default WithExpander;
