#pragma once

#include "Vector.h"
#include "Quaternion.h"
#include "Matrix4.h"

class RigidBody
{
	
	public:	
        Vector3 mPosition;              /* position of center mass */
        Vector3 mMomentum;              /* momentum of ball. */
        Quaternion mOrientation;        /* orientation of the ball. */
        Vector3 mAngularMomentum;       /* angular momentum. */

        Vector3 mVelocity;              /* velocity calculated from momentum */
        Quaternion mSpin;               /* how fast the orientation change. */
        Vector3 mAngularVelocity;       /* angular velocity calculated from angular momentum. */
        Matrix4 mBodyToWorld;           /* body to world coordinates matrix */
        Matrix4 mWorldToBody;           /* world to body coordinates matrix */

        float mRadius;                  /* radius of the ball. */
        float mMass;                    /* mass of the ball. */
        float mInverseMass;             /* inverse of mass used to convert momentum to velocity */
        float mInertiaTensor;           /* inertia tensor of the ball */
        float mInverseInertiaTensor;    /* inverse of the inertia tensor used to convert angular momentum to angular velocity */

		RigidBody();
		~RigidBody();
		void CalculateNewValues();
};