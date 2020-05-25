import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './follow.reducer';
import { IFollow } from 'app/shared/model/follow.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFollowUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FollowUpdate = (props: IFollowUpdateProps) => {
  const [userProfileId, setUserProfileId] = useState('0');
  const [followerId, setFollowerId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { followEntity, userProfiles, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/follow');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUserProfiles();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.followDate = convertDateTimeToServer(values.followDate);

    if (errors.length === 0) {
      const entity = {
        ...followEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="socialNetworkBackendApp.follow.home.createOrEditLabel">
            <Translate contentKey="socialNetworkBackendApp.follow.home.createOrEditLabel">Create or edit a Follow</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : followEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="follow-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="follow-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="followDateLabel" for="follow-followDate">
                  <Translate contentKey="socialNetworkBackendApp.follow.followDate">Follow Date</Translate>
                </Label>
                <AvInput
                  id="follow-followDate"
                  type="datetime-local"
                  className="form-control"
                  name="followDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.followEntity.followDate)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="follow-userProfile">
                  <Translate contentKey="socialNetworkBackendApp.follow.userProfile">User Profile</Translate>
                </Label>
                <AvInput
                  id="follow-userProfile"
                  type="select"
                  className="form-control"
                  name="userProfile.id"
                  value={isNew ? userProfiles[0] && userProfiles[0].id : followEntity.userProfile?.id}
                  required
                >
                  {userProfiles
                    ? userProfiles.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="follow-follower">
                  <Translate contentKey="socialNetworkBackendApp.follow.follower">Follower</Translate>
                </Label>
                <AvInput
                  id="follow-follower"
                  type="select"
                  className="form-control"
                  name="follower.id"
                  value={isNew ? userProfiles[0] && userProfiles[0].id : followEntity.follower?.id}
                  required
                >
                  {userProfiles
                    ? userProfiles.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/follow" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  userProfiles: storeState.userProfile.entities,
  followEntity: storeState.follow.entity,
  loading: storeState.follow.loading,
  updating: storeState.follow.updating,
  updateSuccess: storeState.follow.updateSuccess,
});

const mapDispatchToProps = {
  getUserProfiles,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FollowUpdate);
