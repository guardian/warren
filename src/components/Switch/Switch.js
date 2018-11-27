import React, { Component } from 'react';
import PropTypes from 'prop-types';

import styles from './Switch.module.css';

export default class Switch extends Component {
	static propTypes = {
		id: PropTypes.string.isRequired,
		checked: PropTypes.bool,
		onChange: PropTypes.func.isRequired,
	};

	onChange(ev) {
		this.props.onChange(ev.target.checked);
	}

	render() {
		return (
			<div className={styles.switch}>
				<input
					ref={i => (this.input = i)}
					id={this.props.id}
					type="checkbox"
					className={styles.checkbox}
					checked={this.props.checked || false}
					onChange={ev => {
						this.onChange(ev);
					}}
				/>
				<div className={styles.target} />
			</div>
		);
	}
}
