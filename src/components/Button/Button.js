import React from 'react';
import { Link } from 'react-router-dom';

import styles from './Button.module.css';

const Button = (
	{ children, to, state, size, href, className, icon, ...rest } = {
		size: 'medium',
	}
) => {
	const Tag = to ? Link : href ? 'a' : 'button';
	return (
		<Tag
			href={href}
			to={to}
			className={[styles.root, styles[`size-${size}`], className]
				.filter(_ => _ !== null)
				.join(' ')}
			{...rest}
		>
			{icon}
			{children && <span className={styles.text}>{children}</span>}
		</Tag>
	);
};

export default Button;
