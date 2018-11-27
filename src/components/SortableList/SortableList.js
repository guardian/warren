import React, { Component } from 'react';
import PropTypes from 'prop-types';

import styles from './SortableList.module.css';

const getPath = (draggableIndex, dragTargetIndex) => {
	const path = [];
	let isGoingDown = false;
	if (draggableIndex !== dragTargetIndex && dragTargetIndex >= 0) {
		isGoingDown = draggableIndex < dragTargetIndex;
		const diff = isGoingDown
			? dragTargetIndex - draggableIndex
			: draggableIndex - dragTargetIndex;

		for (let i = 0; i < diff; i++) {
			path.push(isGoingDown ? dragTargetIndex - i : dragTargetIndex + i);
		}
	}

	return { isGoingDown, path };
};

export default class SortableList extends Component {
	state = {
		blocks: [],
		draggedIndexAt: -1,
		dragTargetIndexAt: -1,
	};

	draggableHeight = 20;

	onDragOver(dragTargetIndexAt) {
		this.setState({ dragTargetIndexAt });
	}

	onDragStart(draggedIndexAt) {
		this.setState({ draggedIndexAt });
	}

	onDragEnd(draggedIndexAt, dragTargetIndexAt) {
		this.setState({
			dragTargetIndexAt: -1,
			draggedIndexAt: -1,
		});

		let spliced = [...this.props.children];
		spliced.splice(dragTargetIndexAt, 0, ...spliced.splice(draggedIndexAt, 1));

		this.props.onUpdate(draggedIndexAt, dragTargetIndexAt, spliced);
	}

	static propTypes = {
		children: PropTypes.node.isRequired,
		className: PropTypes.string,
		hasOwnDragRegion: PropTypes.bool,
		onUpdate: PropTypes.func.isRequired,
	};

	render() {
		const { draggedIndexAt, dragTargetIndexAt } = this.state;
		const { children, className, hasOwnDragRegion } = this.props;
		const { isGoingDown, path } = getPath(draggedIndexAt, dragTargetIndexAt);
		const displacement = isGoingDown
			? `translateY(-${this.draggableHeight}px)`
			: `translateY(${this.draggableHeight}px)`;

		return children.map((item, index) => {
			return (
				<div
					key={item.key}
					draggable={!hasOwnDragRegion}
					className={[styles.root, className].filter(_ => _ !== null).join(' ')}
					data-is-dragged={draggedIndexAt === index}
					ref={divElement => {
						if (divElement && draggedIndexAt === index)
							this.draggableHeight = divElement.offsetHeight;
					}}
					data-displace={path.includes(index) ? displacement : 'none'}
					data-animation={draggedIndexAt >= 0}
					style={{
						transform: path.includes(index) ? displacement : null,
					}}
					onDragEnd={() => {
						this.onDragEnd(draggedIndexAt, dragTargetIndexAt);
					}}
					onDragStart={() => {
						this.onDragStart(index);
					}}
					onDragOver={() => {
						this.onDragOver(index);
					}}
				>
					{item}
				</div>
			);
		});
	}
}
