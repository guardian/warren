import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
	MdKeyboardArrowUp,
	MdKeyboardArrowDown,
	MdMoreVert,
} from 'react-icons/md';

import WithDropdownMenu from 'components/composables/WithDropdownMenu/WithDropdownMenu';
import WithContextMenu from 'components/composables/WithContextMenu/WithContextMenu';
import MenuItem from 'components/Menu/MenuItem';
import Button from 'components/Button/Button';
import WithExpander from 'components/composables/WithExpander/WithExpander';

import styles from './Card.module.css';

export default class Card extends Component {
	static propTypes = {
		title: PropTypes.string,
		children: PropTypes.node.isRequired,
		accent: PropTypes.string,
	};

	state = {
		isOpen: true,
		deleting: false,
	};

	flipCollapseState() {
		this.setState(s => ({
			isOpen: !s.isOpen,
		}));
	}

	render() {
		const {
			title,
			children,
			onClose,
			draggable,
			accent,
			compact,
			menuItems,
			toolbar,
		} = this.props;
		const { isOpen } = this.state;

		const menu = (
			<>
				{menuItems &&
					menuItems.map(({ label, func }) => (
						<MenuItem key={label} click={func} {...{ label }} />
					))}
				{menuItems && <MenuItem type="separator" />}
				{onClose && (
					<MenuItem
						click={() => {
							onClose();
						}}
						label={'Delete card'}
					/>
				)}
			</>
		);

		const Title = ({ onToggle, isOpen }) => (
			<WithContextMenu menu={onClose && menu}>
				<div className={styles.title} draggable={draggable}>
					<Button
						className={styles.titleButton}
						type="supertransparent"
						onClick={() => {
							onToggle();
						}}
						icon={isOpen ? <MdKeyboardArrowUp /> : <MdKeyboardArrowDown />}
					>
						<span>{title ? title : 'Card'}</span>
					</Button>
					{toolbar && <div className={styles.titleButton}>{toolbar}</div>}
					{(menuItems || onClose) && (
						<WithDropdownMenu
							proxy={() => (
								<Button className={styles.titleButton} type="supertransparent">
									<MdMoreVert />
								</Button>
							)}
						>
							{menu}
						</WithDropdownMenu>
					)}
				</div>
			</WithContextMenu>
		);

		return (
			<div
				className={compact ? styles.cardCompact : styles.card}
				data-with-accent={accent !== null}
				style={{ borderLeftColor: accent }}
			>
				<WithExpander
					isOpen={isOpen}
					Header={Title}
					onToggle={() => {
						this.flipCollapseState();
					}}
				>
					<div className={styles.contents}>{children}</div>
				</WithExpander>
			</div>
		);
	}
}
