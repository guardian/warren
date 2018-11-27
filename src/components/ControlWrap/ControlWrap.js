import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { MdHelpOutline } from 'react-icons/md';

import styles from './ControlWrap.module.css';

export default class ControlWrap extends Component {
	static propTypes = {
		title: PropTypes.string.isRequired,
		help: PropTypes.string,
		children: PropTypes.node.isRequired,
	};

	render() {
		const { title, htmlFor, children, help, vertical } = this.props;
		return (
			<label
				className={vertical ? styles.verticalWrap : styles.wrap}
				title={help}
				htmlFor={htmlFor}
			>
				<header data-has-help={help !== null} className={styles.title}>
					{title}
					{help && <MdHelpOutline />}
				</header>
				<div>{children}</div>
			</label>
		);
	}
}
