import React, { Component, Fragment } from 'react';

import PopOver from 'components/PopOver/PopOver';
import Menu from 'components/Menu/Menu';

class WithContextMenu extends Component {
	state = {
		isOpen: false,
		openAt: [0, 0],
	};

	setOpen(isOpen, ev) {
		this.setState({
			isOpen,
		});
		if (ev) {
			this.setState({
				openAt: [ev.clientX, ev.clientY],
			});
		}
	}

	render() {
		const { isOpen, openAt } = this.state;
		const { children, menu } = this.props;

		if (!menu) return children;
		return (
			<Fragment>
				{React.cloneElement(children, {
					onContextMenu: ev => {
						this.setOpen(true, ev);
					},
				})}
				{isOpen && (
					<PopOver
						left={openAt[0]}
						top={openAt[1]}
						origin={'left'}
						onClose={() => {
							this.setOpen(false);
						}}
					>
						<Menu isPopover>{menu}</Menu>
					</PopOver>
				)}
			</Fragment>
		);
	}
}

export default WithContextMenu;
