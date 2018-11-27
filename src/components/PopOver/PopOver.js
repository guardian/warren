import React, { Component } from 'react';
import { createPortal } from 'react-dom';

import onClickOutside from 'react-onclickoutside';

import styles from './PopOver.module.css';

class PopOver extends Component {
	handleClickOutside = () => {
		this.props.onClose();
	};

	render() {
		const { children, top, left, origin } = this.props;

		return createPortal(
			<div className={styles.holder} style={{ top, left }} data-origin={origin}>
				<div className={styles.popover}>{children}</div>
			</div>,
			document.querySelector('#overlays')
		);
	}
}

export default onClickOutside(PopOver);
