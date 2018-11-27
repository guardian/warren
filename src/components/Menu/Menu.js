import React, { Children } from 'react';

import styles from './Menu.module.css';

const Menu = ({ children, isPopover }) => (
	<ul className={isPopover ? styles.pop : styles.root}>
		{Children.map(children, item => (
			<li>{item}</li>
		))}
	</ul>
);

export default Menu;
