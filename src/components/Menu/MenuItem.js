import React from 'react';

import styles from './Menu.module.css';
import { withRouter } from 'react-router';
import { MdCheckCircle } from 'react-icons/md';

const Separator = () => <hr className={styles.separator} />;

const onClickFn = ({ click, to, history }) =>
	click ? click() : to ? history.push(to) : null;

const MenuItemTag = ({ href, to, click, type, children, history }) => {
	const Tag = href ? 'a' : 'button';
	return (
		<Tag
			className={styles.item}
			data-type={type}
			{...{ href }}
			onClick={() => {
				onClickFn({ click, to, history });
			}}
		>
			{children}
		</Tag>
	);
};

const MenuItem = (
	{ href, to, click, type, label, sublabel, icon, checked, history } = {
		type: 'normal',
	}
) => {
	if (type === 'separator') return <Separator />;
	return (
		<MenuItemTag {...{ href, to, click, type, history }}>
			<span className={styles.flex}>
				<span>
					{checked && <MdCheckCircle className={styles.check} />}

					{icon}
					{label}
				</span>
				{sublabel !== null && (
					<span className={styles.sublabel}>{sublabel}</span>
				)}
			</span>
		</MenuItemTag>
	);
};

export default withRouter(MenuItem);
