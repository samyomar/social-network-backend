import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './profile-photos.reducer';
import { IProfilePhotos } from 'app/shared/model/profile-photos.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfilePhotosDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProfilePhotosDetail = (props: IProfilePhotosDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { profilePhotosEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="socialNetworkBackendApp.profilePhotos.detail.title">ProfilePhotos</Translate> [
          <b>{profilePhotosEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="image">
              <Translate contentKey="socialNetworkBackendApp.profilePhotos.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {profilePhotosEntity.image ? (
              <div>
                {profilePhotosEntity.imageContentType ? (
                  <a onClick={openFile(profilePhotosEntity.imageContentType, profilePhotosEntity.image)}>
                    <img
                      src={`data:${profilePhotosEntity.imageContentType};base64,${profilePhotosEntity.image}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {profilePhotosEntity.imageContentType}, {byteSize(profilePhotosEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="uploadedDate">
              <Translate contentKey="socialNetworkBackendApp.profilePhotos.uploadedDate">Uploaded Date</Translate>
            </span>
          </dt>
          <dd>
            {profilePhotosEntity.uploadedDate ? (
              <TextFormat value={profilePhotosEntity.uploadedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="title">
              <Translate contentKey="socialNetworkBackendApp.profilePhotos.title">Title</Translate>
            </span>
          </dt>
          <dd>{profilePhotosEntity.title}</dd>
          <dt>
            <Translate contentKey="socialNetworkBackendApp.profilePhotos.userProfile">User Profile</Translate>
          </dt>
          <dd>{profilePhotosEntity.userProfile ? profilePhotosEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/profile-photos" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profile-photos/${profilePhotosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ profilePhotos }: IRootState) => ({
  profilePhotosEntity: profilePhotos.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfilePhotosDetail);
