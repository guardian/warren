import React from 'react';
import { MdHome } from 'react-icons/md';
import { withRouter } from 'react-router-dom';
import history from 'history.js';
import Button from 'components/Button/Button';
import styles from './Header.module.css';

export default withRouter(({ location, toolset, project, onProjectClick }) => {
	const isRoot = location.pathname === '/';

	const onClick = () => {
		if (!isRoot) {
			history.push('/');
		}
	};

	return (
		<header className={styles.head}>
			{!isRoot && (
				<div className={[styles.chip].join(' ')} data-link-home="true">
					<Button
						className={styles.backArrow}
						type="transparent"
						onClick={onClick}
						icon={<MdHome />}
					/>
				</div>
			)}
			<div className={styles.chip} data-elastic={true}>
				{project && (
					<Button
						type="transparent"
						className={styles.project}
						to={`/projects/${project.id}`}
					>
						{project.thumbnail && <img src={project.thumbnail} alt="" />}
						{project.name && (
							<div className={styles.titles}>
								<div className={styles.title}>{project.name}</div>
							</div>
						)}
					</Button>
				)}
			</div>
			{toolset && (
				<div className={[styles.toolset, styles.chip].join(' ')}>{toolset}</div>
			)}
		</header>
	);
});
