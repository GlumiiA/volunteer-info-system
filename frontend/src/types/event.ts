export enum EventType {
  INDIVIDUAL = 'INDIVIDUAL',
  MASS = 'MASS',
}

export interface EventEntry {
  // А надо ли id?
  id: number
  type: EventType
  title: string
  description: string
  volunteersRequired: number
  ageRestriction: number
  dateStart: string
  dateEnd: string
  address?: string
  headerImage?: string
}
