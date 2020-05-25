import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './follow.reducer';
import { IFollow } from 'app/shared/model/follow.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFollowDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FollowDetail = (props: IFollowDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { followEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="socialNetworkBackendApp.follow.detail.title">Follow</Translate> [<b>{followEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="followDate">
              <Translate contentKey="socialNetworkBackendApp.follow.followDate">Follow Date</Translate>
            </span>
          </dt>
          <dd>{followEntity.followDate ? <TextFormat value={followEntity.followDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="socialNetworkBackendApp.follow.userProfile">User Profile</Translate>
          </dt>
          <dd>{followEntity.userProfile ? followEntity.userProfile.id : ''}</dd>
          <dt>
            <Translate contentKey="socialNetworkBackendApp.follow.follower">Follower</Translate>
          </dt>
          <dd>{followEntity.follower ? followEntity.follower.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/follow" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/follow/${followEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ follow }: IRootState) => ({
  followEntity: follow.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FollowDetail);
