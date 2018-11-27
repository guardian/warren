import React, { Component } from 'react';
import PropTypes from 'prop-types';

import styles from './ControlGroupWrap.module.css';

export default class ControlGroupWrap extends Component {
	static propTypes = {
		withPadding: PropTypes.bool,
		title: PropTypes.string,
		children: PropTypes.node.isRequired,
	};

	render() {
		const { title, children, horizontal } = this.props;
		return (
			<div className={styles.wrap} data-horizontal={horizontal}>
				{title && <header className={styles.header}>{title}</header>}
				<div className={styles.children}>{children}</div>
			</div>
		);
	}
}
