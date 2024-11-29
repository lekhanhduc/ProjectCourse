import React from 'react';
import moment from 'moment';
import { CNavItem, CNavLink } from '@coreui/react';
import CIcon from '@coreui/icons-react';
import { cilBell } from '@coreui/icons';
import './notification.css'; // Ensure custom CSS is applied

export const NotificationDropdownAdmin = ({ notifications = [], unreadCount = 0, markAsRead }) => {
    const timeAgo = (createdAt) => {
        return moment(createdAt).fromNow();
    };

    return (
        <CNavItem className="nav-item dropdown mx-2 position-relative">
            <CNavLink
                href="#"
                className="btn btn-light rounded-circle d-flex align-items-center justify-content-center position-relative"
                id="notificationDropdown"
                data-bs-toggle="dropdown"
                aria-expanded="false"
            >
                <CIcon icon={cilBell} size="lg" />
                {unreadCount > 0 && (
                    <span className="adm-notification-badge">{unreadCount}</span>
                )}
            </CNavLink>
            <ul className="dropdown-menu dropdown-menu-end p-3 adm-notification-dropdown shadow-lg" aria-labelledby="notificationDropdown">
                {notifications.length === 0 ? (
                    <li className="dropdown-item text-center text-muted">No new notifications</li>
                ) : (
                    notifications.map((notification) => (
                        <li key={notification.id}
                            className={`adm-notification-item ${notification.isRead ? 'read' : 'unread'}`}
                            style={{ padding: '10px 0', borderBottom: '1px solid #ddd' }}
                        >
                            <img
                                src={notification.avatarUrl || "https://bootdey.com/img/Content/avatar/avatar7.png"}
                                alt="Sender Avatar"
                                className="rounded-circle me-3"
                                style={{ width: '45px', height: '45px' }}
                            />
                            <div>
                                <h6 style={{ fontWeight: notification.isRead ? 'normal' : 'bold' }}>
                                    {notification.title}
                                </h6>
                                <small className="text-muted d-block mb-1">{timeAgo(notification.createdAt)}</small>
                                <button
                                    className="btn btn-sm btn-link"
                                    onClick={() => markAsRead(notification.id)}
                                >
                                    <i className="fa fa-check-circle"></i> Mark as read
                                </button>
                            </div>
                        </li>
                    ))
                )}
            </ul>
        </CNavItem>
    );
};
