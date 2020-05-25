import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './follow.reducer';
import { IFollow } from 'app/shared/model/follow.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFollowProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Follow = (props: IFollowProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { followList, match, loading } = props;
  return (
    <div>
      <h2 id="follow-heading">
        <Translate contentKey="socialNetworkBackendApp.follow.home.title">Follows</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="socialNetworkBackendApp.follow.home.createLabel">Create new Follow</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {followList && followList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.follow.followDate">Follow Date</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.follow.userProfile">User Profile</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.follow.follower">Follower</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {followList.map((follow, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${follow.id}`} color="link" size="sm">
                      {follow.id}
                    </Button>
                  </td>
                  <td>{follow.followDate ? <TextFormat type="date" value={follow.followDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{follow.userProfile ? <Link to={`user-profile/${follow.userProfile.id}`}>{follow.userProfile.id}</Link> : ''}</td>
                  <td>{follow.follower ? <Link to={`user-profile/${follow.follower.id}`}>{follow.follower.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${follow.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${follow.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${follow.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="socialNetworkBackendApp.follow.home.notFound">No Follows found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ follow }: IRootState) => ({
  followList: follow.entities,
  loading: follow.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Follow);
