import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const UserProfile = (props: IUserProfileProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { userProfileList, match, loading } = props;
  return (
    <div>
      <h2 id="user-profile-heading">
        <Translate contentKey="socialNetworkBackendApp.userProfile.home.title">User Profiles</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="socialNetworkBackendApp.userProfile.home.createLabel">Create new User Profile</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {userProfileList && userProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.firstName">First Name</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.lastName">Last Name</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.age">Age</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.gender">Gender</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.country">Country</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.nativeLang">Native Lang</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.userProfile.following2">Following 2</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userProfileList.map((userProfile, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${userProfile.id}`} color="link" size="sm">
                      {userProfile.id}
                    </Button>
                  </td>
                  <td>{userProfile.firstName}</td>
                  <td>{userProfile.lastName}</td>
                  <td>{userProfile.age}</td>
                  <td>
                    <Translate contentKey={`socialNetworkBackendApp.Gender.${userProfile.gender}`} />
                  </td>
                  <td>{userProfile.country}</td>
                  <td>
                    <Translate contentKey={`socialNetworkBackendApp.Language.${userProfile.nativeLang}`} />
                  </td>
                  <td>{userProfile.user ? userProfile.user.id : ''}</td>
                  <td>
                    {userProfile.following2s
                      ? userProfile.following2s.map((val, j) => (
                          <span key={j}>
                            <Link to={`user-profile/${val.id}`}>{val.id}</Link>
                            {j === userProfile.following2s.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userProfile.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userProfile.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userProfile.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="socialNetworkBackendApp.userProfile.home.notFound">No User Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ userProfile }: IRootState) => ({
  userProfileList: userProfile.entities,
  loading: userProfile.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserProfile);
