import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './profile-photos.reducer';
import { IProfilePhotos } from 'app/shared/model/profile-photos.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfilePhotosProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProfilePhotos = (props: IProfilePhotosProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { profilePhotosList, match, loading } = props;
  return (
    <div>
      <h2 id="profile-photos-heading">
        <Translate contentKey="socialNetworkBackendApp.profilePhotos.home.title">Profile Photos</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="socialNetworkBackendApp.profilePhotos.home.createLabel">Create new Profile Photos</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {profilePhotosList && profilePhotosList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.image">Image</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.uploadedDate">Uploaded Date</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.userProfile">User Profile</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {profilePhotosList.map((profilePhotos, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${profilePhotos.id}`} color="link" size="sm">
                      {profilePhotos.id}
                    </Button>
                  </td>
                  <td>
                    {profilePhotos.image ? (
                      <div>
                        {profilePhotos.imageContentType ? (
                          <a onClick={openFile(profilePhotos.imageContentType, profilePhotos.image)}>
                            <img
                              src={`data:${profilePhotos.imageContentType};base64,${profilePhotos.image}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {profilePhotos.imageContentType}, {byteSize(profilePhotos.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {profilePhotos.uploadedDate ? (
                      <TextFormat type="date" value={profilePhotos.uploadedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{profilePhotos.title}</td>
                  <td>
                    {profilePhotos.userProfile ? (
                      <Link to={`user-profile/${profilePhotos.userProfile.id}`}>{profilePhotos.userProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${profilePhotos.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profilePhotos.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profilePhotos.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="socialNetworkBackendApp.profilePhotos.home.notFound">No Profile Photos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ profilePhotos }: IRootState) => ({
  profilePhotosList: profilePhotos.entities,
  loading: profilePhotos.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfilePhotos);
